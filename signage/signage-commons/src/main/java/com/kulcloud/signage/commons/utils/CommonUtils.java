package com.kulcloud.signage.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kulcloud.signage.commons.dto.KeyValue;

public class CommonUtils {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	private static final InetAddress OUTBOUND_ADDRESS = getLocalHostLANAddress();
	
	public static String getOutboundIp() {
		if(OUTBOUND_ADDRESS != null) {
			return OUTBOUND_ADDRESS.getHostAddress();
		} else {
			return null;
		}
	}
	
	public static String getHostName() {
		if(OUTBOUND_ADDRESS != null) {
			return OUTBOUND_ADDRESS.getHostName();
		} else {
			return null;
		}
	}
	
	public static InetAddress getOutboundInetAddress() {
		return OUTBOUND_ADDRESS;
	}
	
	private static InetAddress getLocalHostLANAddress() {
	    try {
	        InetAddress candidateAddress = null;
	        // Iterate all NICs (network interface cards)...
	        for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // Iterate all IP addresses assigned to each card...
	            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {

	                    if (inetAddr.isSiteLocalAddress()) {
	                        // Found non-loopback site-local address. Return it immediately...
	                        return inetAddr;
	                    }
	                    else if (candidateAddress == null) {
	                        // Found non-loopback address, but not necessarily site-local.
	                        // Store it as a candidate to be returned if site-local address is not subsequently found...
	                        candidateAddress = inetAddr;
	                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
	                        // only the first. For subsequent iterations, candidate will be non-null.
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            // We did not find a site-local address, but we found some other non-loopback address.
	            // Server might have a non-site-local address assigned to its NIC (or it might be running
	            // IPv6 which deprecates the "site-local" concept).
	            // Return this non-loopback candidate address...
	            return candidateAddress;
	        }
	        // At this point, we did not find a non-loopback address.
	        // Fall back to returning whatever InetAddress.getLocalHost() returns...
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        if (jdkSuppliedAddress == null) {
	            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
	        }
	        return jdkSuppliedAddress;
	    }
	    catch (Exception e) {
	        return null;
	    }
	}
	
	public static Map<?, ?> convertToMap(Object object) {
		if(object == null) {
			return null;
		} else if(object instanceof Map) {
			return (Map<?, ?>) object;
		} else {
			try {
				return CommonConstants.mapper.convertValue(object, Map.class);
			} catch (Throwable e) {
				return null;
			}
		}
	}
	
	public static <T> T convertMapToBean(Class<T> clazz, Map<?, ?> map) throws Exception {
		if(map == null) {
			return null;
		}
		
		T bean = clazz.newInstance();
		Map<String, Method> setMethods = new HashMap<>();
		for (Method method : clazz.getMethods()) {
			if(method.getName().startsWith("set")) {
				setMethods.put(method.getName(), method);
			}
		}
		
		// set 메서드가 하나 이상 없으면 null 반환
		if(setMethods.size() == 0) {
			return null;
		}
		
		Method set;
		for (Object key : map.keySet()) {
			String pascal = convertToPascalCase(key.toString());
			set = setMethods.get("set" + pascal);
			if(set == null || set.getParameterCount() != 1) {
				continue;
			}
			
			try {
				set.invoke(bean, convertTo(set.getParameters()[0].getType(), map.get(key)));
			} catch (Exception ignored) {
				logger.debug(ignored.getMessage() + " in convertMapToBean");
			}
		}
		
		return bean;
	}
	
	public static Object convertTo(Class<?> clazz, Object value) {
		if(clazz.equals(Boolean.class)) {
			return convertToBoolean(value);
		} else if(clazz.equals(Byte.class)) {
			return convertToByte(value);
		} else if(clazz.equals(Short.class)) {
			return convertToShort(value);
		} else if(clazz.equals(Integer.class)) {
			return convertToInt(value);
		} else if(clazz.equals(Long.class)) {
			return convertToLong(value);
		} else if(clazz.equals(Float.class)) {
			return convertToFloat(value);
		} else if(clazz.equals(BigInteger.class)) {
			return convertToBigInteger(value);
		} else if(clazz.equals(String.class)) {
			return convertToString(value);
		} else if(clazz.equals(Date.class)) {
			return convertToDate(value);
		} else {
			return CommonConstants.mapper.convertValue(value, clazz);
		}
	}
	
	public static Boolean convertToBoolean(Object object) {
		if(object == null) {
			return false;
		} else if(object instanceof Number) {
			return ((Number)object).byteValue() > 0 ? true : false;
		} else if(object instanceof Boolean) {
			return ((Boolean) object).booleanValue();
		} else {
			return Boolean.parseBoolean(object.toString());
		}
	}
	
	public static Byte convertToByte(Object object) {
		if(object == null) {
			return 0;
		} else if(object instanceof Number) {
			return ((Number)object).byteValue();
		} else if(object instanceof Boolean) {
			return (byte) ((Boolean) object ? 1 : 0);
		} else {
			return Byte.parseByte(object.toString());
		}
	}
	
	public static Short convertToShort(Object object) {
		if(object == null) {
			return null;
		} else if(object instanceof Number) {
			return ((Number)object).shortValue();
		} else {
			return Short.parseShort(object.toString());
		}
	}
	
	public static Integer convertToInt(Object object) {
		if(object == null) {
			return 0;
		} else if(object instanceof Number) {
			return ((Number)object).intValue();
		} else {
			return Integer.parseInt(object.toString());
		}
	}
	
	public static Long convertToLong(Object object) {
		if(object == null) {
			return (long) 0;
		} else if(object instanceof Number) {
			return ((Number)object).longValue();
		} else {
			return Long.parseLong(object.toString());
		}
	}

	public static Float convertToFloat(Object object) {
		if(object == null) {
			return 0f;
		} else if(object instanceof Number) {
			return ((Number)object).floatValue();
		} else {
			return Float.parseFloat(object.toString());
		}
	}
	
	public static Double convertToDouble(Object object) {
		if(object == null) {
			return 0d;
		} else if(object instanceof Number) {
			return ((Number)object).doubleValue();
		} else {
			return Double.parseDouble(object.toString());
		}
	}
	
	public static BigInteger convertToBigInteger(Object object) {
		if(object == null) {
			return null;
		} else {
			return new BigInteger(object.toString());
		}
	}
	
	public static String convertToString(Object object) {
		return convertToString(object, null);
	}
	
	public static String convertToString(Object object, String argSuffix) {
		String suffix = argSuffix;
		if(suffix == null) {
			suffix = "";
		}
		
		if(object == null) {
			return null;
		} else if (object instanceof Number){
			return CommonConstants.nf.format(object) + suffix;
		} else if (object instanceof String){
			String str = object.toString().trim();
			StringBuffer sb = new StringBuffer(str);
			if(!str.endsWith(suffix)) {
				sb.append(suffix);
			}
			
			return sb.toString();
		} else {
			try {
				return CommonConstants.mapper.writeValueAsString(object);
			} catch (JsonProcessingException e) {
				return null;
			}
		}
	}
	
	public static Date convertToDate(Object object) {
		if(object == null) {
			throw new IllegalArgumentException("input value is null");
		} else if(object instanceof Date) {
			return (Date) object;
		} else {
			try {
				String str = object.toString();
				return Date.from(LocalDateTime.parse(str, 
						        DateTimeFormatter.ofPattern(CommonConstants.dtf))
						        .atZone(ZoneId.systemDefault()).toInstant());
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	public static Date convertToDate(Object object, Date defaultValue) {
        if(object == null) {
            return defaultValue;
        } else if(object instanceof Date) {
            return (Date) object;
        } else {
            try {
                String str = object.toString();
                return Date.from(LocalDateTime.parse(str, 
                                DateTimeFormatter.ofPattern(CommonConstants.dtf))
                                .atZone(ZoneId.systemDefault()).toInstant());
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
	
	public static String convertToCamelCase(String string) {
		String pascal = convertToPascalCase(string);
		if(pascal.length() > 0) {
			if(pascal.length() > 1) {
				return Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);
			} else {
				return pascal.toLowerCase();
			}
		} else {
			return pascal;
		}
	}
	
	public static String convertToPascalCase(String string) {
		String[] splitStr = string.split("_");
		StringBuilder sb = new StringBuilder();
		for (String str : splitStr) {
			str = str.trim();
			if(str.length() > 0) {
				sb.append(Character.toUpperCase(str.charAt(0)));
				if(str.length() > 1) {
					sb.append(str.substring(1));
				}
			}
		}
		
		return sb.toString();
	}
	
	public static String convertToSnakeCase(String string) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			if(Character.isUpperCase(string.charAt(i)) && i > 0) {
				sb.append('_');
			}
			
			sb.append(Character.toLowerCase(string.charAt(i)));
		}
		
		return sb.toString();
	}
	
	public static RestTemplate getRestTemplate(boolean https, boolean offSsl, int timeout) {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    HttpClientBuilder builder = HttpClients.custom();
        if(https) {
        	if(offSsl) {
        		SSLContextBuilder sslcontext = new SSLContextBuilder();
                try {
					sslcontext.loadTrustMaterial(null, new TrustSelfSignedStrategy());
					builder.setSSLContext(sslcontext.build());
				} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ignored) {
					logger.debug(ignored.getMessage() + " in getRestTemplate");
				}
        	}
        	
        	builder.setSSLHostnameVerifier(new NoopHostnameVerifier());
		}
	    requestFactory.setHttpClient(builder.build());
	    requestFactory.setConnectTimeout(timeout);
        return new RestTemplate(requestFactory);
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public static short floatToShort(float x) {
	    if (x < Short.MIN_VALUE) {
	        return Short.MIN_VALUE;
	    }
	    if (x > Short.MAX_VALUE) {
	        return Short.MAX_VALUE;
	    }
	    return (short) Math.round(x);
	}
	
	public static Date nowDate() {
		return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDateTime nowLocalDateTime() {
		return LocalDateTime.now(ZoneId.systemDefault());
	}
	
	public static long getTime(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<Class<? extends T>> scan(String packageName, Class<T> clazz){
    	ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
    	    @Override
    	    protected boolean isCandidateComponent(MetadataReader metadataReader) {
    	    	String[] intfNames = metadataReader.getClassMetadata().getInterfaceNames();
    	    	for (String intfName : intfNames) {
					if(intfName.equals(clazz.getName())) {
						return true;
					}
				}
    	    	
    	    	return false;
    	    }
    	};
    	
    	List<Class<? extends T>> result = new ArrayList<>();
    	for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
    		try {
				result.add((Class<? extends T>)Class.forName(bd.getBeanClassName()));
			} catch (ClassNotFoundException e) {
				logger.error("Cannot find a class: " + bd.getBeanClassName(), e);
			}
    	}
    	
    	return result;
    }
	
	public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
		DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
 
        byte[] buffer = broadcastMessage.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buffer, buffer.length, address, 4445);
        socket.send(packet);
        socket.close();
	}
	
	public static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
	    List<InetAddress> broadcastList = new ArrayList<>();
	    Enumeration<NetworkInterface> interfaces 
	      = NetworkInterface.getNetworkInterfaces();
	    while (interfaces.hasMoreElements()) {
	        NetworkInterface networkInterface = interfaces.nextElement();
	 
	        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
	            continue;
	        }
	 
	        networkInterface.getInterfaceAddresses().stream() 
	          .map(a -> a.getBroadcast())
	          .filter(Objects::nonNull)
	          .forEach(broadcastList::add);
	    }
	    
	    return broadcastList;
	}
	
	public static List<KeyValue> convertMapToKeyValues(Map<?, ?> map) {
		List<KeyValue> keyValues = new ArrayList<>(map.size());
		for (Entry<?, ?> entry : map.entrySet()) {
			keyValues.add(new KeyValue(entry.getKey().toString(), entry.getValue()));
		}
		
		return keyValues;
	}
	
	public static void unzip(File zipFile, File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFileForZipEntry(destDir, zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
     
    private static File newFileForZipEntry(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
         
        return destFile;
    }
}

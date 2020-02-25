package com.kulcloud.signage.cms.wowza;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kulcloud.signage.cms.streaming.StreamingServerProxy;
import com.kulcloud.signage.cms.streaming.data.LiveContent;
import com.kulcloud.signage.cms.streaming.data.VodContent;
import com.kulcloud.signage.cms.wowza.dto.ApplicationConfig;
import com.kulcloud.signage.cms.wowza.dto.StreamConfigurationConfig;
import com.kulcloud.signage.commons.ssh.InputStreamSource;
import com.kulcloud.signage.commons.ssh.SshService;
import com.kulcloud.signage.commons.utils.CommonConstants;

@Service
public class WowzaProxy implements StreamingServerProxy {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ObjectMapper mapper = new ObjectMapper();
	private String urlFormat;
	
	private String all = "*";
	
	private String appPath;
	private String storageDir;
	private boolean sshUsed;
	
	private SshService ssh;
	private WowzaRestTemplate restTemplate;
	
	public WowzaProxy(
			@Value("${wowza.host:localhost}") String wowzaHost,
			@Value("${wowza.serverName:_defaultServer_}") String serverName,
			@Value("${wowza.vhostName:_defaultVHost_}") String vhostName,
			@Value("${wowza.stream.port:1935}") int wowzaStreamPort,
			@Value("${wowza.stream.ssl:false}") boolean ssl,
    		@Value("${wowza.storageDir:/usr/local/WowzaStreamingEngine/content}") String storageDir,
			@Value("${wowza.ssh.used:false}") boolean sshUsed,
			@Value("${wowza.ssh.port:22}") int sshPort,
			@Value("${wowza.ssh.user:}") String sshUser,
			@Value("${wowza.ssh.password:}") String sshPassword,
			@Value("${wowza.ssh.key:}") String sshKeyLocation,
			@Autowired WowzaRestTemplate restTemplate) {
		
		this.appPath = String.format("/v2/servers/%s/vhosts/%s/applications", serverName, vhostName);
		this.storageDir = storageDir;
		this.restTemplate = restTemplate;
		this.urlFormat = (ssl ? CommonConstants.HTTPS : CommonConstants.HTTP) + CommonConstants.SCHEME +
				wowzaHost + CommonConstants.COLON + wowzaStreamPort + "/%s/%s/manifest.mpd";
		
		if(sshUsed) {
			this.sshUsed = sshUsed;
			ssh = new SshService(wowzaHost, sshPort, sshUser, sshPassword, sshKeyLocation);
		}
	}
	
	@Override
	public boolean createStorage(String tenantId) {
		if(!isExist(tenantId)) {
			
			String bodyVod = null;
			String bodyLive = null;
			try {
				ApplicationConfig configVod = new ApplicationConfig();
				configVod.setName(getNameForVod(tenantId));
				configVod.setAppType(AppType.vod.name());
				configVod.setClientStreamReadAccess(all);
				configVod.setClientStreamWriteAccess(all);
				configVod.setDescription(getDescriptionForVod(tenantId));
				StreamConfigurationConfig streamConfig = new StreamConfigurationConfig();
				streamConfig.setStreamType(StreamType.file.getName());
				streamConfig.setStorageDir(storageDir + File.separator + getNameForVod(tenantId));
				configVod.setStreamConfig(streamConfig);
				bodyVod = mapper.writeValueAsString(configVod);
				
				ApplicationConfig configLive = new ApplicationConfig();
				configLive.setName(getNameForLive(tenantId));
				configLive.setAppType(AppType.live.name());
				configLive.setClientStreamReadAccess(all);
				configLive.setClientStreamWriteAccess(all);
				configLive.setDescription(getDescriptionForLive(tenantId));
				streamConfig = new StreamConfigurationConfig();
				streamConfig.setStreamType(StreamType.live.getName());
				streamConfig.setStorageDir(storageDir + File.separator + getNameForLive(tenantId));
				configLive.setStreamConfig(streamConfig);
				bodyLive = mapper.writeValueAsString(configLive);
				
				Future<Void> futureVod = restTemplate.asyncRequest(getPathForVod(tenantId), HttpMethod.POST, bodyVod, Void.class);
				Future<Void> futureLive = restTemplate.asyncRequest(getPathForLive(tenantId), HttpMethod.POST, bodyLive, Void.class);
				try {
					futureVod.get();
					futureLive.get();
					mkDir(storageDir + File.separator + getNameForVod(tenantId));
					mkDir(storageDir + File.separator + getNameForLive(tenantId));
					
					return true;
				} catch (InterruptedException | ExecutionException e) {
					logger.error("Cannot create a streaming storage for " + tenantId, e);
					return false;
				}
				
			} catch (JsonProcessingException e) {
				logger.error("Cannot create a streaming storage for " + tenantId, e);
				return false;
			}
		} else {
			logger.info("Already exist a streaming storage for " + tenantId);
			return true;
		}
	}

	private boolean mkDir(String directory) {
		if(sshUsed) {
			return ssh.mkDir(directory);
		} else {
			try {
				FileUtils.forceMkdir(new File(directory));
				return true;
			} catch (IOException e) {
				logger.error("Cannot create a directory of streaming storage: " + directory, e);
				return false;
			}
		}
	}

	@Override
	public boolean deleteStorage(String tenantId) {
		Future<Void> futureVod = restTemplate.asyncRequest(getPathForVod(tenantId), HttpMethod.DELETE, null, Void.class);
		Future<Void> futureLive = restTemplate.asyncRequest(getPathForLive(tenantId), HttpMethod.DELETE, null, Void.class);
		try {
			futureVod.get();
			futureLive.get();
			rmDir(storageDir + File.separator + getNameForVod(tenantId));
			rmDir(storageDir + File.separator + getNameForLive(tenantId));
			
			return true;
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Cannot delete a streaming storage for " + tenantId, e);
			return false;
		}
	}
	
	private boolean rmDir(String directory) {
		if(sshUsed) {
			return ssh.rmDir(directory);
		} else {
			try {
				FileUtils.forceDelete(new File(directory));
				return true;
			} catch (IOException e) {
				logger.error("Cannot delete a directory of streaming storage: " + directory, e);
				return false;
			}
		}
	}
	
	public boolean isExist(String tenantId) {
		try {
			Future<ApplicationConfig> futureVod = restTemplate.asyncRequest(getPathForVod(tenantId), HttpMethod.GET, null, ApplicationConfig.class);
			Future<ApplicationConfig> futureLive = restTemplate.asyncRequest(getPathForLive(tenantId), HttpMethod.GET, null, ApplicationConfig.class);
			if(futureVod.get() == null || futureLive.get() == null) {
				return false;
			}
			
			return true;
		} catch (RestClientException | InterruptedException | ExecutionException e) {
			return false;
		}
	}

	@Override
	public boolean addVod(VodContent content, MultipartFile file) {
		String directory = storageDir + File.separator + getNameForVod(content.getId().getTenantId());
		if(sshUsed) {
			try {
				if (ssh.uploadFile(new InputStreamSource(file), directory)) {
					content.setUrl(String.format(urlFormat, content.getId().getTenantId(),
							MediaPrefix.getStreamName(content.getFileName())));
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				logger.error("Cannot save a file in the remote directory (" + directory + ")", e);
				return false;
			}
		} else {
			try {
				File dir = new File(directory);
				if(!dir.exists()) {
					FileUtils.forceMkdir(dir);
				}
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(dir, file.getOriginalFilename()));
				return true;
			} catch (IOException e) {
				logger.error("Cannot save a file in the local directory (" + directory + ")", e);
				return false;
			}
		}
	}

	@Override
	public boolean deleteVod(VodContent content) {
		String filePath = storageDir + File.separator + getNameForVod(content.getId().getTenantId()) + File.separator + content.getFileName();
		if(sshUsed) {
			return ssh.deleteFile(filePath);
		} else {
			try {
				File file = new File(filePath);
				if(file.exists()) {
					FileUtils.forceDelete(file);
				}
				return true;
			} catch (IOException e) {
				logger.error("Cannot delete a file (" + filePath + ")", e);
				return false;
			}
		}
	}

	@Override
	public boolean addLive(LiveContent content) {
		return false;
	}

	@Override
	public boolean deleteLive(LiveContent content) {
		return false;
	}
	
	private String getPathForVod(String tenantId) {
		return appPath + "/" + getNameForVod(tenantId);
	}
	
	private String getPathForLive(String tenantId) {
		return appPath + "/" + getNameForLive(tenantId);
	}
	
	private String getNameForVod(String tenantId) {
		return tenantId + "_" + "vod";
	}
	
	private String getNameForLive(String tenantId) {
		return tenantId + "_" + "live";
	}

	private String getDescriptionForVod(String tenantId) {
		return "A storage of VOD for " + tenantId;
	}

	private String getDescriptionForLive(String tenantId) {
		return "A storage of Live for " + tenantId;
	}
	
	@PreDestroy
	private void destory() {
		if(ssh != null) {
			try {
				ssh.disconnect();
			} catch (IOException ignored) {}
		}
	}
}

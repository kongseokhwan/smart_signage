package com.kulcloud.signage.commons.ssh;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.xfer.FileSystemFile;

public class SshService {
	private static final Logger logger = LoggerFactory.getLogger(SshService.class);
	
	private SSHClient ssh;
	
	private String sshHost;
	private int sshPort;
	private String sshUser;
	private String sshPassword;
	private String keyLocation;
	
	public SshService(String sshHost, int sshPort, String sshUser, String sshPassword, String keyLocation) {
		super();
		this.sshHost = sshHost;
		this.sshPort = sshPort;
		this.sshUser = sshUser;
		this.sshPassword = sshPassword;
		this.keyLocation = keyLocation;
	}
	
	public synchronized void connect() throws IOException {
		if(ssh == null || !ssh.isConnected()) {
			DefaultConfig defaultConfig = new DefaultConfig();
	        defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
			ssh = new SSHClient(defaultConfig);
			ssh.addHostKeyVerifier((hostname, p, key) -> true);
			ssh.connect(sshHost, sshPort);
			ssh.getConnection().getKeepAlive().setKeepAliveInterval(5); //every 60sec
			if(StringUtils.isAnyBlank(sshUser, sshPassword)) {
				if(StringUtils.isAnyBlank(sshUser, keyLocation)) {
					ssh.authPublickey(System.getProperty("user.name"));
				} else {
					ssh.authPublickey(sshUser, keyLocation);
				}
			} else {
				ssh.authPassword(sshUser, sshPassword);
			}
		}
	}
	
	public boolean mkDir(String directory) {
		return exec("mkdir -p " + directory) == null ? false : true;
	}
	
	public boolean rmDir(String directory) {
		return exec("rm -r " + directory) == null ? false : true;
	}
	
	public String exec(String command) {
		try {
			connect();
		} catch (IOException e1) {
			return null;
		}
		
		if(ssh != null) {
			Session session = null;
			try {
				session = ssh.startSession();
				final Command cmd = session.exec(command);
				String result = IOUtils.readFully(cmd.getInputStream()).toString();
				logger.info(command + '\n' + result);
				return result;
			} catch (IOException e) {
				logger.error("Cannot execute a command(': " + command + "')", e);
				return null;
			} finally {
				if(session != null) {
					try {
						session.close();
					} catch (TransportException | ConnectionException ignored) {}
				}
			}
		} else {
			logger.error("Must connect to ssh");
			return null;
		}
	}
	
	public List<String> getFileList(String directory) {
		String result = exec("ls -p " + directory + " | grep -v /");
		if(result != null) {
			String[] list = StringUtils.split(result, '\n');
			return Arrays.asList(list);
		} else {
			return Collections.emptyList();
		}
	}
	
	public boolean uploadFile(String directory, String fileName, String remoteDirectory) {
		try {
			connect();
		} catch (IOException e1) {
			return false;
		}
		
		if(ssh != null) {
			try {
				ssh.useCompression();
				ssh.newSCPFileTransfer().upload(new FileSystemFile(new File(directory, fileName)), remoteDirectory);
				return true;
			} catch (IOException e) {
				logger.error("Cannot upload the file: " + fileName, e);
				return false;
			}
		} else {
			logger.error("Must connect to ssh");
			return false;
		}
	}
	
	public boolean uploadFile(InputStreamSource localSource, String remoteDirectory) {
		try {
			connect();
		} catch (IOException e1) {
			return false;
		}
		
		if(ssh != null) {
			try {
				ssh.useCompression();
				ssh.newSCPFileTransfer().upload(localSource, remoteDirectory);
				return true;
			} catch (IOException e) {
				logger.error("Cannot upload the file: " + localSource.getName(), e);
				return false;
			}
		} else {
			logger.error("Must connect to ssh");
			return false;
		}
	}
	
	public boolean downloadFile(String remoteFilePath, String localDirectory) {
		try {
			connect();
		} catch (IOException e1) {
			return false;
		}
		
		if(ssh != null) {
			try {
				ssh.useCompression();
				ssh.newSCPFileTransfer().download(remoteFilePath, new FileSystemFile(localDirectory));
				return true;
			} catch (IOException e) {
				logger.error("Cannot download the file: " + remoteFilePath, e);
				return false;
			}
		} else {
			logger.error("Must connect to ssh");
			return false;
		}
	}
	
	public boolean deleteFile(String remoteFilePath) {
		return exec("rm " + remoteFilePath) == null ? false : true;
	}
	
	public void disconnect() throws IOException {
		if(ssh != null && ssh.isConnected()) {
			ssh.disconnect();
			ssh = null;
		}
	}
}

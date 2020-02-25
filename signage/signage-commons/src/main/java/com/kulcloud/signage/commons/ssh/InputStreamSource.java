package com.kulcloud.signage.commons.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.springframework.web.multipart.MultipartFile;

import net.schmizz.sshj.xfer.LocalFileFilter;
import net.schmizz.sshj.xfer.LocalSourceFile;

public class InputStreamSource implements LocalSourceFile {
	
	private long modifiedTime;
	private String name;
	private long length;
	private InputStream input;
	
	public InputStreamSource(String name, long length, InputStream input) {
		this.modifiedTime = System.currentTimeMillis();
		this.name = name;
		this.length = length;
		this.input = input;
	}
	
	public InputStreamSource(MultipartFile file) throws IOException {
		this.modifiedTime = file.getResource().lastModified();
		this.name = file.getOriginalFilename();
		this.length = file.getSize();
		this.input = file.getInputStream();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getLength() {
		return length;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return input;
	}

	@Override
	public int getPermissions() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public Iterable<? extends LocalSourceFile> getChildren(LocalFileFilter filter) throws IOException {
		return Collections.emptyList();
	}

	@Override
    public boolean providesAtimeMtime() {
        return true;
    }

    @Override
    public long getLastAccessTime()
            throws IOException {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public long getLastModifiedTime()
            throws IOException {
        return modifiedTime / 1000;
    }

}

package com.kulcloud.signage.tenant.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tb_playlist")
public class Playlist extends EditedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "playlist_id")
	@JsonProperty("playlist_id")
	private Long playlistId;
	private String title;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_playlist_content",
               joinColumns = @JoinColumn(name = "playlist_id"),
               inverseJoinColumns = @JoinColumn(name = "content_id"))
	private List<Content> contents;

	public Long getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(Long playlistId) {
		this.playlistId = playlistId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
	
	public void addContent(Content content) {
		List<Content> contents;
		if(this.contents == null) {
			if(this.playlistId == null) {
				contents = new ArrayList<> ();
				this.contents = contents;
			} else {
				contents = getContents();
			}
		} else {
			contents = this.contents;
		}
		
		boolean contain  = false;
		for (Content contentItem : contents) {
			if(contentItem.getContentId().equals(content.getContentId())) {
				contain = true;
			}
		}
		
		if(!contain) {
			contents.add(content);
		}
	}
	
	public boolean removeContent(String contentId) {
		List<Content> contents = this.getContents();
		for (Content content : contents) {
			if(content.getContentId().equals(contentId)) {
				return contents.remove(content);
			}
		}
		
		return false;
	}
	
}

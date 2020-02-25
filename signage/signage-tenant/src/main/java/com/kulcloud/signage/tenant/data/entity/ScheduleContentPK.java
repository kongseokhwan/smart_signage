package com.kulcloud.signage.tenant.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class ScheduleContentPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "schedule_id")
	@JsonProperty("schedule_id")
	private Long scheduleId;
	@Column(name = "play_content_id")
	@JsonProperty("play_content_id")
	private Long playContentId;
	@Column(name = "playlist_yn")
	@JsonProperty("playlist_yn")
	private Boolean playlistYn;

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getPlayContentId() {
		return playContentId;
	}

	public void setPlayContentId(Long playContentId) {
		this.playContentId = playContentId;
	}

	public Boolean getPlaylistYn() {
		return playlistYn;
	}

	public void setPlaylistYn(Boolean playlistYn) {
		this.playlistYn = playlistYn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playContentId == null) ? 0 : playContentId.hashCode());
		result = prime * result + ((playlistYn == null) ? 0 : playlistYn.hashCode());
		result = prime * result + ((scheduleId == null) ? 0 : scheduleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleContentPK other = (ScheduleContentPK) obj;
		if (playContentId == null) {
			if (other.playContentId != null)
				return false;
		} else if (!playContentId.equals(other.playContentId))
			return false;
		if (playlistYn == null) {
			if (other.playlistYn != null)
				return false;
		} else if (!playlistYn.equals(other.playlistYn))
			return false;
		if (scheduleId == null) {
			if (other.scheduleId != null)
				return false;
		} else if (!scheduleId.equals(other.scheduleId))
			return false;
		return true;
	}

}

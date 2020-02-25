package com.kulcloud.signage.tenant.data.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_schedule_content")
public class ScheduleContent {

	@EmbeddedId
	private ScheduleContentPK id;
	private int sequence;

	public ScheduleContentPK getId() {
		return id;
	}

	public void setId(ScheduleContentPK id) {
		this.id = id;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}

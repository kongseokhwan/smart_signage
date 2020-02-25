package com.kulcloud.signage.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.ScheduleContent;
import com.kulcloud.signage.tenant.data.entity.ScheduleContentPK;

public interface ScheduleContentRepository extends JpaRepository<ScheduleContent, ScheduleContentPK> {

}

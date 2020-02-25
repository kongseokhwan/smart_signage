package com.kulcloud.signage.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}

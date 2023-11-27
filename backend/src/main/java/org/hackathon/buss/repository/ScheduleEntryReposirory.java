package org.hackathon.buss.repository;

import org.hackathon.buss.model.ScheduleEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleEntryReposirory extends JpaRepository<ScheduleEntry, Long> {
}

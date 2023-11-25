package org.hackathon.buss.model.stats;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hackathon.buss.model.ScheduleEntry;

@Entity
@Data
public class ScheduleEntryStats {
    @Id
    long scheduleEntryStatsId;

    @OneToOne
    ScheduleEntry scheduleEntry;

    int ridesCount;
    double averageTimeInMinutes;
}

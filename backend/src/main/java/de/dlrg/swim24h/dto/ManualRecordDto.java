package de.dlrg.swim24h.dto;

import java.time.LocalDateTime;

public record ManualRecordDto(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer laneNo,
        String note
) {}

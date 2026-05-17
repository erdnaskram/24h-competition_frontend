package de.dlrg.swim24h.dto;

import java.time.LocalDateTime;

public record ScheduleDto(
        LocalDateTime plannedStart,
        LocalDateTime plannedEnd,
        Integer plannedLane
) {}

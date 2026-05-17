package de.dlrg.swim24h.dto;

public record LaneInfoDto(
        Integer lane,
        int participants,
        double averageTimeSeconds
) {}

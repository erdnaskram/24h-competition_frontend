package de.dlrg.swim24h.dto;

public record ParticipantStatsDto(
        long id,
        long lapCount,
        long swimDistance,
        Double averageTimeSeconds,
        long lastChangedSecondsAgo
) {}

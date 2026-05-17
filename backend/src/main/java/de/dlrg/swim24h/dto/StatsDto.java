package de.dlrg.swim24h.dto;

public record StatsDto(
        long totalSwimmers,
        long activeSwimmers,
        long totalDistanceMeters
) {}

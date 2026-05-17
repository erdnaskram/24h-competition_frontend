package de.dlrg.swim24h.dto;

public record ParticipantDto(
        long id,
        String name,
        int age,
        String gender,
        int lanes,
        boolean isActive,
        Integer lane,
        Double averageTimeSeconds,
        long lastChangesSecondsAgo,
        String clothingType,
        String clothingColor,
        String googlesColor,
        String hatColor,
        String hairColor,
        boolean tattoo,
        boolean headphones,
        String remarks
) {}

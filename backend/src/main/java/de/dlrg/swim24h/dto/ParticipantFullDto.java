package de.dlrg.swim24h.dto;

public record ParticipantFullDto(
        long id,
        String firstName,
        String lastName,
        int age,
        String gender,
        Integer lane,
        Boolean isActive,
        long swimDistance,
        String team,
        String family,
        String startGroup,
        String remarks,
        CharacteristicsDto characteristics
) {}

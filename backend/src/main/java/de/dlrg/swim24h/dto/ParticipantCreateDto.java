package de.dlrg.swim24h.dto;

public record ParticipantCreateDto(
        String firstName,
        String lastName,
        int age,
        String gender,
        String team,
        String family,
        String startGroup,
        String remarks
) {}

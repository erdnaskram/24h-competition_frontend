package de.dlrg.swim24h.dto;

public record CharacteristicsDto(
        String clothingType,
        String clothingColor,
        String googlesColor,
        String hatColor,
        String hairColor,
        Boolean beard,
        Boolean tattoo,
        Boolean headphones,
        String remarks
) {}

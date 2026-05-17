package de.dlrg.swim24h.controller;

import de.dlrg.swim24h.dto.ParticipantDto;
import de.dlrg.swim24h.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/SearchByPrefix/{letter}")
    public List<ParticipantDto> searchByPrefix(@PathVariable String letter) {
        return participantService.searchByPrefix(letter);
    }

    @GetMapping("/SearchAsYouType")
    public List<ParticipantDto> searchAsYouType(@RequestParam String filter) {
        return participantService.searchAsYouType(filter);
    }
}

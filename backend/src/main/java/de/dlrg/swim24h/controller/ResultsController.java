package de.dlrg.swim24h.controller;

import de.dlrg.swim24h.dto.ParticipantDto;
import de.dlrg.swim24h.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResultsController {

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/results")
    public List<ParticipantDto> getResults() {
        return participantService.getResultsList();
    }
}

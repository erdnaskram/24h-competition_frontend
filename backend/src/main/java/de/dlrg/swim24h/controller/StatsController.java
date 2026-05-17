package de.dlrg.swim24h.controller;

import de.dlrg.swim24h.dto.LaneInfoDto;
import de.dlrg.swim24h.dto.ParticipantBasicDto;
import de.dlrg.swim24h.dto.StatsDto;
import de.dlrg.swim24h.dto.ParticipantStatsDto;
import de.dlrg.swim24h.repository.ParticipantRepository;
import de.dlrg.swim24h.service.ParticipantService;
import de.dlrg.swim24h.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private ParticipantRepository participantRepo;

    @Autowired
    private ParticipantService participantService;

    /** GET /api/families — alle Familien */
    @GetMapping("/families")
    public List<String> getFamilies() {
        return participantRepo.findDistinctFamilies();
    }

    /** GET /api/teams — alle Teams */
    @GetMapping("/teams")
    public List<String> getTeams() {
        return participantRepo.findDistinctTeams();
    }

    /** GET /api/teams/{team}/members — Mitglieder eines Teams */
    @GetMapping("/teams/{team}/members")
    public List<ParticipantBasicDto> getTeamMembers(@PathVariable String team) {
        return participantService.getByTeam(team);
    }

    /** GET /api/families/{family}/members — Mitglieder einer Familie */
    @GetMapping("/families/{family}/members")
    public List<ParticipantBasicDto> getFamilyMembers(@PathVariable String family) {
        return participantService.getByFamily(family);
    }

    /** GET /api/startgroups — alle Startgruppen */
    @GetMapping("/startgroups")
    public List<String> getStartGroups() {
        return participantService.getStartGroups();
    }

    /** GET /api/startgroups/{group}/members — Mitglieder einer Startgruppe */
    @GetMapping("/startgroups/{group}/members")
    public List<ParticipantBasicDto> getStartGroupMembers(@PathVariable String group) {
        return participantService.getByStartGroup(group);
    }

    /** GET /api/lanes/{lane}/participants — Alle Teilnehmer einer Bahn */
    @GetMapping("/lanes/{lane}/participants")
    public List<ParticipantBasicDto> getLaneParticipants(@PathVariable int lane) {
        return participantService.getByLaneBasic(lane);
    }

    /** GET /api/schedule — Alle Teilnehmer mit geplanter Startzeit */
    @GetMapping("/schedule")
    public List<ParticipantBasicDto> getSchedule() {
        return participantService.getSchedule();
    }

    /** GET /api/stats — Gesamtstatistik */
    @GetMapping("/stats")
    public StatsDto getStats() {
        return statsService.getStats();
    }

    /** GET /api/lane-info — Bahn-Informationen */
    @GetMapping("/lane-info")
    public List<LaneInfoDto> getLaneInfo() {
        return statsService.getLaneInfo();
    }
}

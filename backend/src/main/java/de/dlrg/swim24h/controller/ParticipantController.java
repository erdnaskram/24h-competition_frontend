package de.dlrg.swim24h.controller;

import de.dlrg.swim24h.dto.*;
import de.dlrg.swim24h.entity.RecordedTime;
import de.dlrg.swim24h.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    /** GET /api/participants — alle Teilnehmer (Basic-Format) */
    @GetMapping
    public List<ParticipantBasicDto> getAll() {
        return participantService.getAllBasic();
    }

    /** GET /api/participants/{id} — einzelner Teilnehmer mit Characteristics */
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantFullDto> getById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(participantService.getById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** POST /api/participants — neuen Teilnehmer anlegen */
    @PostMapping
    public ResponseEntity<ParticipantFullDto> create(@RequestBody ParticipantCreateDto dto) {
        return ResponseEntity.ok(participantService.createParticipant(dto));
    }

    /** PUT /api/participants/{id} — Teilnehmerdaten aktualisieren (ohne Characteristics) */
    @PutMapping("/{id}")
    public ResponseEntity<ParticipantFullDto> update(@PathVariable long id,
                                                      @RequestBody ParticipantCreateDto dto) {
        try {
            return ResponseEntity.ok(participantService.updateParticipant(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/characteristics — Characteristics aktualisieren */
    @PutMapping("/{id}/characteristics")
    public ResponseEntity<ParticipantDto> updateCharacteristics(@PathVariable long id,
                                                                  @RequestBody CharacteristicsDto dto) {
        try {
            return ResponseEntity.ok(participantService.saveProperties(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/assign — Teilnehmer einer Bahn zuweisen */
    @PutMapping("/{id}/assign")
    public ResponseEntity<ParticipantDto> assignToLane(@PathVariable long id,
                                                        @RequestBody AssignLaneDto dto) {
        try {
            if (dto.lane() == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(participantService.assignToLane(id, dto.lane()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/active — Teilnehmer aktivieren */
    @PutMapping("/{id}/active")
    public ResponseEntity<ParticipantDto> setActive(@PathVariable long id) {
        try {
            return ResponseEntity.ok(participantService.setActive(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/inactive — Teilnehmer deaktivieren */
    @PutMapping("/{id}/inactive")
    public ResponseEntity<ParticipantDto> setInactive(@PathVariable long id) {
        try {
            return ResponseEntity.ok(participantService.setInactive(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/leave — Bahn verlassen */
    @PutMapping("/{id}/leave")
    public ResponseEntity<Void> leaveLane(@PathVariable long id) {
        try {
            participantService.leaveLane(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** POST /api/participants/{id}/count — Runde zählen */
    @PostMapping("/{id}/count")
    public ResponseEntity<ParticipantDto> countLap(@PathVariable int id,
                                                    @RequestBody CountLapDto dto) {
        try {
            return ResponseEntity.ok(participantService.countLap(id, dto.laneNo(), dto.startTime()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** GET /api/participants/{id}/records — RecordedTime-Einträge eines Teilnehmers */
    @GetMapping("/{id}/records")
    public ResponseEntity<List<RecordedTime>> getRecords(@PathVariable long id) {
        return ResponseEntity.ok(participantService.getRecordsByParticipantId(id));
    }

    /** GET /api/participants/search?filter=... — Teilnehmer suchen */
    @GetMapping("/search")
    public List<ParticipantDto> search(@RequestParam String filter) {
        return participantService.searchAsYouType(filter);
    }

    /** GET /api/participants/certificates/pending — Teilnehmer ohne gedruckte Urkunde */
    @GetMapping("/certificates/pending")
    public List<ParticipantBasicDto> getCertificatesPending() {
        return participantService.getCertificatesPending();
    }

    /** GET /api/participants/{id}/stats — Einzelstatistik eines Teilnehmers */
    @GetMapping("/{id}/stats")
    public ResponseEntity<ParticipantStatsDto> getStats(@PathVariable long id) {
        try {
            return ResponseEntity.ok(participantService.getParticipantStats(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** DELETE /api/participants/{id} — Teilnehmer löschen (inkl. aller Rundeneinträge) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        try {
            participantService.deleteParticipant(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** DELETE /api/participants/{id}/records/{recordId} — Einzelnen Rundeneintrag löschen */
    @DeleteMapping("/{id}/records/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable long id, @PathVariable long recordId) {
        try {
            participantService.deleteRecord(id, recordId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** POST /api/participants/{id}/records — Manuellen Rundeneintrag hinzufügen */
    @PostMapping("/{id}/records")
    public ResponseEntity<RecordedTime> addManualRecord(@PathVariable int id,
                                                         @RequestBody ManualRecordDto dto) {
        try {
            return ResponseEntity.ok(participantService.addManualRecord(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/certificate — Urkunde als gedruckt markieren */
    @PutMapping("/{id}/certificate")
    public ResponseEntity<ParticipantFullDto> markCertificatePrinted(@PathVariable long id) {
        try {
            return ResponseEntity.ok(participantService.markCertificatePrinted(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** PUT /api/participants/{id}/schedule — Geplante Zeiten und Bahn setzen */
    @PutMapping("/{id}/schedule")
    public ResponseEntity<ParticipantFullDto> updateSchedule(@PathVariable long id,
                                                              @RequestBody ScheduleDto dto) {
        try {
            return ResponseEntity.ok(participantService.updateSchedule(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

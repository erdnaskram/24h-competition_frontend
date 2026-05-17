package de.dlrg.swim24h.service;

import de.dlrg.swim24h.dto.*;
import de.dlrg.swim24h.entity.Participant;
import de.dlrg.swim24h.entity.RecordedTime;
import de.dlrg.swim24h.repository.ParticipantRepository;
import de.dlrg.swim24h.repository.RecordedTimeRepository;
import de.dlrg.swim24h.sse.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepo;

    @Autowired
    private RecordedTimeRepository recordedTimeRepo;

    @Lazy
    @Autowired
    private SseService sseService;

    // -------------------------------------------------------------------------
    // Internal stats helper
    // -------------------------------------------------------------------------

    private record ParticipantStats(int lanes, Double averageTimeSeconds, long lastChangesSecondsAgo) {}

    private Map<Long, ParticipantStats> buildStatsMap() {
        List<Object[]> rows = recordedTimeRepo.getStatsPerParticipant();
        Map<Long, ParticipantStats> map = new HashMap<>();

        for (Object[] row : rows) {
            Long participantId = ((Number) row[0]).longValue();
            long count = ((Number) row[1]).longValue();
            // AVG(durationSeconds) can be null when all records have null durationSeconds
            Double avgTime = row[2] != null ? ((Number) row[2]).doubleValue() : null;
            // MAX(endTime) may come back as LocalDateTime or java.sql.Timestamp depending on driver
            LocalDateTime lastEndTime = null;
            if (row[3] instanceof LocalDateTime ldt) {
                lastEndTime = ldt;
            } else if (row[3] instanceof Timestamp ts) {
                lastEndTime = ts.toLocalDateTime();
            }

            int lanesCount = (int) (count * 2);

            long secondsAgo = 0;
            if (lastEndTime != null) {
                long nowEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                long lastEpoch = lastEndTime.toEpochSecond(ZoneOffset.UTC);
                secondsAgo = Math.max(0, nowEpoch - lastEpoch);
            }

            map.put(participantId, new ParticipantStats(lanesCount, avgTime, secondsAgo));
        }

        return map;
    }

    // -------------------------------------------------------------------------
    // DTO converters
    // -------------------------------------------------------------------------

    private ParticipantDto toDto(Participant p, ParticipantStats stats) {
        String fullName = (p.getFirstName() != null ? p.getFirstName() : "") + " " + (p.getName() != null ? p.getName() : "");
        int lanes = stats != null ? stats.lanes() : 0;
        Double avgTime = stats != null ? stats.averageTimeSeconds() : null;
        long lastChange = stats != null ? stats.lastChangesSecondsAgo() : 0L;

        return new ParticipantDto(
                p.getId(),
                fullName.trim(),
                p.getAge() != null ? p.getAge() : 0,
                p.getGender() != null ? p.getGender() : "",
                lanes,
                Boolean.TRUE.equals(p.getIsActive()),
                p.getLane(),
                avgTime,
                lastChange,
                p.getClothingType() != null ? p.getClothingType() : "",
                p.getClothingColor() != null ? p.getClothingColor() : "",
                p.getGooglesColor() != null ? p.getGooglesColor() : "",
                p.getHatColor() != null ? p.getHatColor() : "",
                p.getHairColor() != null ? p.getHairColor() : "",
                Boolean.TRUE.equals(p.getTattoo()),
                Boolean.TRUE.equals(p.getHeadphones()),
                p.getRemarks() != null ? p.getRemarks() : ""
        );
    }

    private ParticipantFullDto toFullDto(Participant p, Map<Long, ParticipantStats> statsMap) {
        ParticipantStats stats = statsMap.get(p.getId());
        long lapCount = stats != null ? stats.lanes() / 2L : 0L;
        long swimDistance = lapCount * 50L;

        Boolean beard = null;
        if (p.getBeard() != null) {
            beard = "1".equals(p.getBeard());
        }

        CharacteristicsDto chars = new CharacteristicsDto(
                p.getClothingType() != null ? p.getClothingType() : "",
                p.getClothingColor() != null ? p.getClothingColor() : "",
                p.getGooglesColor() != null ? p.getGooglesColor() : "",
                p.getHatColor() != null ? p.getHatColor() : "",
                p.getHairColor() != null ? p.getHairColor() : "",
                beard,
                Boolean.TRUE.equals(p.getTattoo()),
                Boolean.TRUE.equals(p.getHeadphones()),
                p.getRemarks() != null ? p.getRemarks() : ""
        );

        return new ParticipantFullDto(
                p.getId(),
                p.getFirstName() != null ? p.getFirstName() : "",
                p.getName() != null ? p.getName() : "",
                p.getAge() != null ? p.getAge() : 0,
                p.getGender() != null ? p.getGender() : "",
                p.getLane(),
                p.getIsActive(),
                swimDistance,
                p.getTeam(),
                p.getFamily(),
                p.getStartGroup() != null ? p.getStartGroup() : "",
                p.getRemarks() != null ? p.getRemarks() : "",
                chars
        );
    }

    private ParticipantBasicDto toBasicDto(Participant p, Map<Long, ParticipantStats> statsMap) {
        ParticipantStats stats = statsMap.get(p.getId());
        long lapCount = stats != null ? stats.lanes() / 2L : 0L;
        long swimDistance = lapCount * 50L;

        return new ParticipantBasicDto(
                p.getId(),
                p.getFirstName() != null ? p.getFirstName() : "",
                p.getName() != null ? p.getName() : "",
                p.getAge() != null ? p.getAge() : 0,
                p.getGender() != null ? p.getGender() : "",
                p.getLane(),
                p.getIsActive(),
                swimDistance,
                p.getTeam(),
                p.getFamily(),
                p.getStartGroup() != null ? p.getStartGroup() : ""
        );
    }

    // -------------------------------------------------------------------------
    // SSE helper
    // -------------------------------------------------------------------------

    private Map<String, Object> buildLaneInfo(int laneId) {
        long activeCount = participantRepo.findByLane(laneId).stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .count();
        return Map.of("laneNo", laneId, "activeCount", activeCount);
    }

    // -------------------------------------------------------------------------
    // Query methods
    // -------------------------------------------------------------------------

    public List<ParticipantDto> getByLane(int laneId) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findByLane(laneId).stream()
                .map(p -> toDto(p, statsMap.get(p.getId())))
                .toList();
    }

    public List<ParticipantBasicDto> getByLaneBasic(int laneId) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findByLane(laneId).stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    public List<ParticipantDto> searchByPrefix(String letter) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findByNameStartingWith(letter).stream()
                .map(p -> toDto(p, statsMap.get(p.getId())))
                .toList();
    }

    public List<ParticipantDto> searchAsYouType(String filter) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.searchByName(filter).stream()
                .map(p -> toDto(p, statsMap.get(p.getId())))
                .toList();
    }

    public List<ParticipantBasicDto> getByTeam(String team) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findByTeam(team).stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    public List<ParticipantBasicDto> getByFamily(String family) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findByFamily(family).stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    public List<ParticipantDto> getResultsList() {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findAll().stream()
                .map(p -> toDto(p, statsMap.get(p.getId())))
                .sorted(Comparator.comparingLong(ParticipantDto::id).reversed())
                .toList();
    }

    public List<ParticipantFullDto> getAllFull() {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findAll().stream()
                .map(p -> toFullDto(p, statsMap))
                .toList();
    }

    public List<ParticipantBasicDto> getAllBasic() {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findAll().stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    public ParticipantFullDto getById(long id) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return toFullDto(p, statsMap);
    }

    public List<RecordedTime> getRecordsByParticipantId(long id) {
        return recordedTimeRepo.findByParticipantId(id);
    }

    // -------------------------------------------------------------------------
    // Mutation methods
    // -------------------------------------------------------------------------

    @Transactional
    public ParticipantDto countLap(int id, Integer laneNo, LocalDateTime startTime) {
        Participant p = participantRepo.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));

        RecordedTime rt = new RecordedTime();
        rt.setParticipantId(id);
        rt.setStartTime(startTime != null ? startTime : LocalDateTime.now());
        rt.setEndTime(LocalDateTime.now());
        rt.setLanes(laneNo != null ? laneNo : p.getLane());
        if (startTime != null) {
            long durationSecs = java.time.Duration.between(startTime, rt.getEndTime()).getSeconds();
            rt.setDurationSeconds((int) durationSecs);
        }
        recordedTimeRepo.save(rt);

        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantDto result = toDto(p, statsMap.get(p.getId()));
        if (p.getLane() != null) {
            sseService.publishToLane(p.getLane(), "lapCounted", result);
            sseService.publishGlobal("laneInfoUpdated", buildLaneInfo(p.getLane()));
        }
        return result;
    }

    @Transactional
    public ParticipantDto setInactive(long id) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        p.setIsActive(false);
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantDto result = toDto(p, statsMap.get(p.getId()));
        if (p.getLane() != null) {
            sseService.publishToLane(p.getLane(), "participantDeactivated", result);
            sseService.publishGlobal("laneInfoUpdated", buildLaneInfo(p.getLane()));
        }
        return result;
    }

    @Transactional
    public ParticipantDto setActive(long id) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        p.setIsActive(true);
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantDto result = toDto(p, statsMap.get(p.getId()));
        if (p.getLane() != null) {
            sseService.publishToLane(p.getLane(), "participantActivated", result);
            sseService.publishGlobal("laneInfoUpdated", buildLaneInfo(p.getLane()));
        }
        return result;
    }

    @Transactional
    public void leaveLane(long id) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        Integer previousLane = p.getLane();
        p.setLane(null);
        p.setIsActive(false);
        participantRepo.save(p);
        if (previousLane != null) {
            sseService.publishToLane(previousLane, "participantLeft", Map.of("participantId", id, "laneNo", previousLane));
            sseService.publishGlobal("laneInfoUpdated", buildLaneInfo(previousLane));
        }
    }

    @Transactional
    public ParticipantDto saveProperties(long id, CharacteristicsDto dto) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));

        p.setClothingType(dto.clothingType());
        p.setClothingColor(dto.clothingColor());
        p.setGooglesColor(dto.googlesColor());
        p.setHatColor(dto.hatColor());
        p.setHairColor(dto.hairColor());
        if (dto.beard() != null) {
            p.setBeard(dto.beard() ? "1" : "0");
        }
        p.setTattoo(Boolean.TRUE.equals(dto.tattoo()));
        p.setHeadphones(Boolean.TRUE.equals(dto.headphones()));
        p.setRemarks(dto.remarks());

        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantDto result = toDto(p, statsMap.get(p.getId()));
        if (p.getLane() != null) {
            sseService.publishToLane(p.getLane(), "participantUpdated", result);
        }
        return result;
    }

    @Transactional
    public ParticipantDto assignToLane(long id, int laneId) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        p.setLane(laneId);
        p.setIsActive(true);
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantDto result = toDto(p, statsMap.get(p.getId()));
        sseService.publishToLane(laneId, "participantAssigned", result);
        sseService.publishGlobal("laneInfoUpdated", buildLaneInfo(laneId));
        return result;
    }

    @Transactional
    public ParticipantFullDto createParticipant(ParticipantCreateDto dto) {
        Participant p = new Participant();
        p.setFirstName(dto.firstName());
        p.setName(dto.lastName());
        p.setAge(dto.age());
        p.setGender(dto.gender());
        p.setTeam(dto.team());
        p.setFamily(dto.family());
        p.setStartGroup(dto.startGroup());
        p.setRemarks(dto.remarks());
        p.setIsActive(false);
        p.setRegistrationTime(LocalDateTime.now());
        p.setTattoo(false);
        p.setHeadphones(false);
        p.setCertificatePrinted(false);
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantFullDto result = toFullDto(p, statsMap);
        sseService.publishGlobal("participantCreated", result);
        return result;
    }

    @Transactional
    public ParticipantFullDto updateParticipant(long id, ParticipantCreateDto dto) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        p.setFirstName(dto.firstName());
        p.setName(dto.lastName());
        p.setAge(dto.age());
        p.setGender(dto.gender());
        p.setTeam(dto.team());
        p.setFamily(dto.family());
        p.setStartGroup(dto.startGroup());
        p.setRemarks(dto.remarks());
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantFullDto result = toFullDto(p, statsMap);
        sseService.publishGlobal("participantUpdated", result);
        return result;
    }

    @Transactional
    public void deleteParticipant(long id) {
        if (!participantRepo.existsById(id)) {
            throw new IllegalArgumentException("Participant not found: " + id);
        }
        recordedTimeRepo.deleteByParticipantId(id);
        participantRepo.deleteById(id);
        sseService.publishGlobal("participantDeleted", Map.of("participantId", id));
    }

    @Transactional
    public void deleteRecord(long participantId, long recordId) {
        if (!participantRepo.existsById(participantId)) {
            throw new IllegalArgumentException("Participant not found: " + participantId);
        }
        RecordedTime record = recordedTimeRepo.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found: " + recordId));
        if (record.getParticipantId() == null || record.getParticipantId().longValue() != participantId) {
            throw new IllegalArgumentException("Record does not belong to participant: " + participantId);
        }
        recordedTimeRepo.deleteById(recordId);
    }

    @Transactional
    public RecordedTime addManualRecord(int participantId, ManualRecordDto dto) {
        Participant p = participantRepo.findById((long) participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + participantId));

        RecordedTime rt = new RecordedTime();
        rt.setParticipantId(participantId);
        rt.setStartTime(dto.startTime() != null ? dto.startTime() : LocalDateTime.now());
        rt.setEndTime(dto.endTime() != null ? dto.endTime() : LocalDateTime.now());
        rt.setLanes(dto.laneNo() != null ? dto.laneNo() : p.getLane());
        if (dto.startTime() != null && dto.endTime() != null) {
            long secs = java.time.Duration.between(dto.startTime(), dto.endTime()).getSeconds();
            rt.setDurationSeconds((int) secs);
        }
        rt.setManualEntry(true);
        return recordedTimeRepo.save(rt);
    }

    public ParticipantStatsDto getParticipantStats(long id) {
        if (!participantRepo.existsById(id)) {
            throw new IllegalArgumentException("Participant not found: " + id);
        }
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        ParticipantStats stats = statsMap.get(id);
        long lapCount = stats != null ? stats.lanes() / 2L : 0L;
        long swimDistance = lapCount * 50L;
        Double avgTime = stats != null ? stats.averageTimeSeconds() : null;
        long lastChanged = stats != null ? stats.lastChangesSecondsAgo() : 0L;
        return new ParticipantStatsDto(id, lapCount, swimDistance, avgTime, lastChanged);
    }

    @Transactional
    public ParticipantFullDto markCertificatePrinted(long id) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        p.setCertificatePrinted(true);
        p.setCertificateDateTime(LocalDateTime.now());
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return toFullDto(p, statsMap);
    }

    public List<ParticipantBasicDto> getCertificatesPending() {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findCertificatesPending().stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    public List<String> getStartGroups() {
        return participantRepo.findDistinctStartGroups();
    }

    public List<ParticipantBasicDto> getByStartGroup(String startGroup) {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findByStartGroup(startGroup).stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    public List<ParticipantBasicDto> getSchedule() {
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return participantRepo.findScheduled().stream()
                .map(p -> toBasicDto(p, statsMap))
                .toList();
    }

    @Transactional
    public ParticipantFullDto updateSchedule(long id, ScheduleDto dto) {
        Participant p = participantRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id));
        p.setPlannedStart(dto.plannedStart());
        p.setPlannedEnd(dto.plannedEnd());
        p.setPlannedLane(dto.plannedLane());
        participantRepo.save(p);
        Map<Long, ParticipantStats> statsMap = buildStatsMap();
        return toFullDto(p, statsMap);
    }
}

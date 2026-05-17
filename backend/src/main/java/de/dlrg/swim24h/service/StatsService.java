package de.dlrg.swim24h.service;

import de.dlrg.swim24h.dto.LaneInfoDto;
import de.dlrg.swim24h.dto.StatsDto;
import de.dlrg.swim24h.repository.ParticipantRepository;
import de.dlrg.swim24h.repository.RecordedTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    @Autowired
    private ParticipantRepository participantRepo;

    @Autowired
    private RecordedTimeRepository recordedTimeRepo;

    public StatsDto getStats() {
        long total = participantRepo.count();
        long active = participantRepo.countByIsActive(true);
        long totalDistance = recordedTimeRepo.countAll() * 50L;
        return new StatsDto(total, active, totalDistance);
    }

    public List<LaneInfoDto> getLaneInfo() {
        List<Integer> lanes = participantRepo.findDistinctLanes();
        return lanes.stream().map(laneNo -> {
            long count = participantRepo.countByLane(laneNo);
            // Average time could be enhanced with a native query per lane,
            // but keeping simple for now
            double avgTime = 0.0;
            return new LaneInfoDto(laneNo, (int) count, avgTime);
        }).toList();
    }

    public long getTotalLanes() {
        return recordedTimeRepo.countAll() * 2L;
    }
}

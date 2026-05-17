package de.dlrg.swim24h.repository;

import de.dlrg.swim24h.entity.RecordedTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordedTimeRepository extends JpaRepository<RecordedTime, Long> {

    List<RecordedTime> findByParticipantId(Long participantId);

    long countByParticipantId(Long participantId);

    @Query("SELECT r.participantId, COUNT(r), AVG(r.durationSeconds), MAX(r.endTime) FROM RecordedTime r GROUP BY r.participantId")
    List<Object[]> getStatsPerParticipant();

    @Query("SELECT COUNT(r) FROM RecordedTime r")
    long countAll();

    void deleteByParticipantId(Long participantId);
}

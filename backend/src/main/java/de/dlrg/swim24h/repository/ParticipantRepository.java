package de.dlrg.swim24h.repository;

import de.dlrg.swim24h.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByLane(Integer lane);

    long countByLane(Integer lane);

    long countByIsActive(Boolean isActive);

    @Query("SELECT DISTINCT p.lane FROM Participant p WHERE p.lane IS NOT NULL")
    List<Integer> findDistinctLanes();

    @Query("SELECT DISTINCT p.family FROM Participant p WHERE p.family IS NOT NULL AND p.family <> ''")
    List<String> findDistinctFamilies();

    @Query("SELECT DISTINCT p.team FROM Participant p WHERE p.team IS NOT NULL AND p.team <> ''")
    List<String> findDistinctTeams();

    @Query("SELECT p FROM Participant p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :f, '%')) OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :f, '%'))")
    List<Participant> searchByName(@Param("f") String filter);

    @Query("SELECT p FROM Participant p WHERE p.name LIKE CONCAT(:letter, '%') OR p.firstName LIKE CONCAT(:letter, '%')")
    List<Participant> findByNameStartingWith(@Param("letter") String letter);

    List<Participant> findByTeam(String team);

    List<Participant> findByFamily(String family);

    @Query("SELECT DISTINCT p.startGroup FROM Participant p WHERE p.startGroup IS NOT NULL AND p.startGroup <> ''")
    List<String> findDistinctStartGroups();

    List<Participant> findByStartGroup(String startGroup);

    @Query("SELECT p FROM Participant p WHERE p.certificatePrinted IS NULL OR p.certificatePrinted = false")
    List<Participant> findCertificatesPending();

    @Query("SELECT p FROM Participant p WHERE p.plannedStart IS NOT NULL ORDER BY p.plannedStart ASC")
    List<Participant> findScheduled();
}

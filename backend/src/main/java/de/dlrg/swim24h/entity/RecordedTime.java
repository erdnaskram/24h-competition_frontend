package de.dlrg.swim24h.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "RecordedTime")
@Getter
@Setter
@NoArgsConstructor
public class RecordedTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "ParticipantId")
    private Integer participantId;

    @Column(name = "StartTime")
    private LocalDateTime startTime;

    @Column(name = "EndTime")
    private LocalDateTime endTime;

    @Column(name = "Lanes")
    private Integer lanes;

    @Column(name = "DurationSeconds")
    private Integer durationSeconds;

    @Column(name = "ManualEntry")
    private Boolean manualEntry;
}

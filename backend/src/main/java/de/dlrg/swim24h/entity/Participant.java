package de.dlrg.swim24h.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Participant")
@Getter
@Setter
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "BirthDate")
    private LocalDateTime birthDate;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "Lane")
    private Integer lane;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "ClothingType")
    private String clothingType;

    @Column(name = "ClothingColor")
    private String clothingColor;

    @Column(name = "GooglesColor")
    private String googlesColor;

    @Column(name = "HatColor")
    private String hatColor;

    @Column(name = "HairColor")
    private String hairColor;

    @Column(name = "Beard")
    private String beard;

    @Column(name = "Tattoo")
    private Boolean tattoo;

    @Column(name = "Headphones")
    private Boolean headphones;

    @Column(name = "Age")
    private Integer age;

    @Column(name = "Remarks")
    private String remarks;

    @Column(name = "PlannedStart")
    private LocalDateTime plannedStart;

    @Column(name = "PlannedEnd")
    private LocalDateTime plannedEnd;

    @Column(name = "PlannedLane")
    private Integer plannedLane;

    @Column(name = "CertificateDateTime")
    private LocalDateTime certificateDateTime;

    @Column(name = "CertificatePrinted")
    private Boolean certificatePrinted;

    @Column(name = "Family")
    private String family;

    @Column(name = "RegistrationTime")
    private LocalDateTime registrationTime;

    @Column(name = "StartGroup")
    private String startGroup;

    @Column(name = "Team")
    private String team;
}

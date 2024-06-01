package com.examsofbharat.bramhsastra.prithvi.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name= "application_vacancy_details")
public class ApplicationVacancyDetails {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id_ref")
    private String appIdRef;

    @Column(name = "department")
    private String department;

    @Column(name = "general")
    private String general;

    @Column(name = "st")
    private String st;

    @Column(name = "sc")
    private String sc;

    @Column(name = "obc")
    private String obc;

    @Column(name = "female")
    private String female;

    @Column(name = "ex_army")
    private String exArmy;

    @Column(name = "total_vacancy")
    private String totalVacancy;

    @Column(name = "information")
    private String information;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

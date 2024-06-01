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
@Table(name= "application_age_details")
public class ApplicationAgeDetails {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id_ref")
    private String appIdRef;

    @Column(name = "min_age")
    private Double minAge;

    @Column(name = "general_age")
    private Double generalAge;

    @Column(name = "st_age")
    private Double stAge;

    @Column(name = "sc_age")
    private Double scAge;

    @Column(name = "obc_age")
    private Double obcAge;

    @Column(name = "female_age")
    private Double femaleAge;

    @Column(name = "ex_army")
    private Double exArmy;

    @Column(name = "information")
    private String information;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

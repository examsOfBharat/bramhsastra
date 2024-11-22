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
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "general_age")
    private Integer generalAge;

    @Column(name = "st_age")
    private Integer stAge;

    @Column(name = "sc_age")
    private Integer scAge;

    @Column(name = "obc_age")
    private Integer obcAge;

    @Column(name = "female_age")
    private Integer femaleAge;

    @Column(name = "ex_army")
    private Integer exArmy;

    @Column(name = "information")
    private String information;

    @Column(name = "max_normal_dob")
    private Date maxNormalDob;

    @Column(name = "min_normal_dob")
    private Date minNormalDob;

    @Column(name = "extra_date_details")
    private String extraDateDetails;

    @Column(name = "updates")
    private String updates;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

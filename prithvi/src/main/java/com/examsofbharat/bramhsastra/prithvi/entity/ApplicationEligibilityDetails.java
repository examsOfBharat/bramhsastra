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
@Table(name= "application_eligibility_details")
public class ApplicationEligibilityDetails {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id_ref")
    private String appIdRef;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "age_range")
    private String ageRange;

    @Column(name = "experience")
    private String experience;

    @Column(name = "others")
    private String others;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

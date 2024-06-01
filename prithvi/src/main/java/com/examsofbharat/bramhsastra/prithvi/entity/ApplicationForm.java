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
@Table(name= "application_form")
public class ApplicationForm {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "min_qualification")
    private String minQualification;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "sectors")
    private String sectors;

    @Column(name = "province")
    private String province;

    @Column(name = "grade")
    private String grade;

    @Column(name = "gender")
    private String gender;

    @Column(name = "admit_flag")
    private int admitFlag;

    @Column(name = "result_flag")
    private int resultFlag;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}
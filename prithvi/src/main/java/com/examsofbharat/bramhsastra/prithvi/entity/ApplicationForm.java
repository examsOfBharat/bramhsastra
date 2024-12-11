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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "assigned")
    private String id;

    @Column(name = "exam_name")
    private String examName;

    @Column(name = "sort_name")
    private String sortName;

    @Column(name = "min_qualification")
    private String minQualification;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "sectors")
    private String sectors;

    @Column(name = "province")
    private String province;

    @Column(name = "state")
    private String state;

    @Column(name = "grade")
    private String grade;

    @Column(name = "gender")
    private String gender;

    @Column(name = "admit_id")
    private String admitId;

    @Column(name = "result_id")
    private String resultId;

    @Column(name = "total_vacancy")
    private int totalVacancy;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;

    @Column(name = "answer_key_url")
    private String answerKeyUrl;

    @Column(name = "answer_date")
    private Date answerDate;

    @Column(name = "qualification")
    private String qualification;
}

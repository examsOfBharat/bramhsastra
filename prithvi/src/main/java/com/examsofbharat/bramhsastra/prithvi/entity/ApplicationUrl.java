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
@Table(name= "application_url")
public class ApplicationUrl {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id_ref")
    private String appIdRef;

    @Column(name = "official_website")
    private String officialWebsite;

    @Column(name = "notification")
    private String notification;

    @Column(name = "apply")
    private String apply;

    @Column(name = "register")
    private String register;

    @Column(name = "admit_card")
    private String admitCard;

    @Column(name = "result")
    private String result;

    @Column(name = "others")
    private String others;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

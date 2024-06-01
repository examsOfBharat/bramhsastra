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
@Table(name= "application_fee_details")
public class ApplicationFeeDatails {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id_ref")
    private String appIdRef;

    @Column(name = "default_fee")
    private Double defaultFee;

    @Column(name = "general")
    private Double general;

    @Column(name = "st")
    private Double st;

    @Column(name = "sc")
    private Double sc;

    @Column(name = "obc")
    private Double obc;

    @Column(name = "female")
    private Double female;

    @Column(name = "ex_army")
    private Double exArmy;

    @Column(name = "information")
    private String information;

    @Column(name = "last_payment_date")
    private Date lastPaymentDate;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

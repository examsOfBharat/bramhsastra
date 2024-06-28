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
@Table(name= "admin_response_manager")
public class AdminResponseManager {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "response_type")
    private String responseType;

    @Column(name = "response")
    private String response;

    @Column(name = "status")
    private String status;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "admin_name")
    private String admin_name;

    @Column(name = "approver_id")
    private String approverId;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

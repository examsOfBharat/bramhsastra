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
@Table(name= "user_details")
public class UserDetails {

    @Id
    @GenericGenerator(name ="generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    int id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "second_name")
    String secondName;

    @Column(name = "password")
    String passWord;

    @Column(name = "email_id")
    String emailId;

    @Column(name = "enable_flag")
    String enableFlag;

    @Column(name = "user_role")
    String userRole;

    @Column(name = "otp")
    String otp;

    @Column(name = "attempts")
    int attempts;

    @Column(name = "status")
    String status;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "date_Created")
    Date dateCreated;

    @Column(name = "date_modified")
    Date dateModified;
}

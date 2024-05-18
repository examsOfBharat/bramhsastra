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
@Table(name= "user_detail")
public class UserDetails {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String passWord;

    @Column(name = "email_id")
    private String emailId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserDetails.UserRole userRole;

    @Column(name = "otp")
    private String otp;

    @Column(name = "attempt")
    private int attempts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserDetails.Status status = Status.NOT_CREATED;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "approver")
    private String approver;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserDetails.UserStatus userStatus;

    @Column(name = "date_Created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;

    public enum UserRole{
        ADMIN, OWNER, CEO
    }

    public enum Status{
        CREATED, VERIFIED, EXPIRED, NOT_CREATED
    }

    public enum UserStatus{
        APPROVED, REJECTED, PENDING
    }
}

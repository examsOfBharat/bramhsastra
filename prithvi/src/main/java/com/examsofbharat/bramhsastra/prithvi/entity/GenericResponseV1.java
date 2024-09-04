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
@Table(name= "generic_response")
public class GenericResponseV1 {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "app_id_ref")
    private String appIdRef;

    @Column(name = "title")
    private String title;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;

    @Column(name = "show_date")
    private Date showDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "type")
    private String type;
}

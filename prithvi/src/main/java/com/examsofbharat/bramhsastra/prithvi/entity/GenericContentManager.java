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
@Table(name= "generic_content_manager")
public class GenericContentManager {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "form_id_ref")
    private String formIdRef;

    @Column(name = "heading")
    private String heading;

    @Column(name = "body")
    private String body;

    @Column(name = "add_on")
    private String addOn;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;
}

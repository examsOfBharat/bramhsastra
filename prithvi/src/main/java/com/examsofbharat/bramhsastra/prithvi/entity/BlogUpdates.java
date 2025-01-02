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
@Table(name= "blog_updates")
public class BlogUpdates {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name ="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "ref_id")
    private String refId;

    @Column(name = "title")
    private String title;

    @Column(name = "title_value")
    private String titleValue;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;

    @Column(name = "url_flag")
    private Integer urlFlag;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "value")
    private String value;
}

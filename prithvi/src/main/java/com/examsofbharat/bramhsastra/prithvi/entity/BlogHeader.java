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
@Table(name= "blog_header")
public class BlogHeader {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name ="uuid", strategy = "assigned")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_modified")
    private Date dateModified;

    @Column(name = "short_title")
    private String shortTitle;

    @Column(name = "writer")
    private String writer;

    @Column(name = "tags")
    private String tags;

    @Column(name = "thumb_nail")
    private String thumbNail;

    @Column(name = "content_img")
    private String contentImg;

}

package com.examsofbharat.bramhsastra.prithvi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name= "system_property")
public class SystemProperty {

    @Id
    String name;

    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    String value;

    @Column(name = "date_created", nullable = false, columnDefinition = "datetime default now()")
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'HH:mm:ss.SSSZ")
    Date dateCreated = new Date();

    @Column(name = "date_modified", nullable = false, columnDefinition = "datetime default now()")
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'HH:mm:ss.SSSZ")
    Date dateModified = new Date();
}

package com.lookingdev.github.Domain.Entities;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "developers")
public class DeveloperProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "developers_id_seq")
    @SequenceGenerator(name = "developers_id_seq", sequenceName = "developers_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "platform")
    private String platform;

    @Column(name = "username")
    private String username;

    @Column(name = "profileUrl")
    private String profileUrl;

    @Column(name = "reputation")
    private Integer reputation;

    @Type(StringArrayType.class)
    @Column(name = "skills", columnDefinition = "text[]")
    private String[] stringArray;

    @Column(name = "location")
    private String location;

    @Column(name = "lastActivityDate")
    private LocalDateTime lastActivityDate;
}

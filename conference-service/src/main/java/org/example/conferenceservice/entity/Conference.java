package org.example.conferenceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.conferenceservice.model.Keynote;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private TypeConference type;
    private Date date;
    private Double duree;
    private int nombreInscrits;
    private Double score;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "conference")
    private List<Review> reviews;
    private Long keynoteId;
    @Transient
    private Keynote keynote;
}

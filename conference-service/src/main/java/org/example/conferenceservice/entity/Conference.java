package org.example.conferenceservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.conferenceservice.model.Keynote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Conference {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private TypeConference type;
    private Date date;
    private Double duree;
    private int nombreInscrits;
    private Double score;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "conference",fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();
    private Long keynoteId;
    @Transient
    private Keynote keynote;

    public void addReview(Review review) {
        reviews.add(review);
        review.setConference(this);
    }
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setConference(null);
    }
}

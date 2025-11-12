package org.example.conferenceservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.conferenceservice.model.Keynote;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceResponseDTO {
    private Long id;
    private String titre;
    private String type;
    private Date date;
    private Double duree;
    private int nombreInscrits;
    private Double score;
    private List<ReviewResponseDTO> reviews ;
    private Long keynoteId;
    private Keynote keynote;
}

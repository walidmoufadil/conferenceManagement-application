package org.example.conferenceservice.dto;


import lombok.*;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReviewRequestDTO {
    private Date date;
    private String commentaire;
}

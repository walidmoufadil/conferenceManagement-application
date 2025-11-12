package org.example.keynoteservice.dto;

import lombok.*;
import org.example.keynoteservice.entity.Keynote;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class KeynoteRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String fonction;

}

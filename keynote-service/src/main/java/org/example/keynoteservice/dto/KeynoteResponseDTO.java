package org.example.keynoteservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KeynoteResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;

}

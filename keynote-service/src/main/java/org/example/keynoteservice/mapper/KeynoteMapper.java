package org.example.keynoteservice.mapper;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.entity.Keynote;
import org.springframework.stereotype.Service;

@Service
public class KeynoteMapper {
    public  Keynote toEntity(KeynoteRequestDTO dto) {
        return Keynote.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .fonction(dto.getFonction())
                .build();
    }

    public  KeynoteResponseDTO toKeynoteResponseDTO(Keynote keynote) {
        return KeynoteResponseDTO.builder()
                .id(keynote.getId())
                .nom(keynote.getNom())
                .prenom(keynote.getPrenom())
                .email(keynote.getEmail())
                .fonction(keynote.getFonction())
                .build();
    }
}

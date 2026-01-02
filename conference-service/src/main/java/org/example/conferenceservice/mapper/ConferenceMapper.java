package org.example.conferenceservice.mapper;

import org.example.conferenceservice.dto.ConferenceRequestDTO;
import org.example.conferenceservice.dto.ConferenceResponseDTO;
import org.example.conferenceservice.entity.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConferenceMapper {
    @Autowired
    private ReviewMapper reviewMapper;

    public  ConferenceResponseDTO toDTO(Conference conference) {
        ConferenceResponseDTO conferenceResponseDTO = new ConferenceResponseDTO();
        conferenceResponseDTO.setId(conference.getId());
        conferenceResponseDTO.setDate(conference.getDate());
        conferenceResponseDTO.setDuree(conference.getDuree());
        conferenceResponseDTO.setScore(conference.getScore());
        conferenceResponseDTO.setTitre(conference.getTitre());
        conferenceResponseDTO.setNombreInscrits(conference.getNombreInscrits());
        conferenceResponseDTO.setType(conference.getType().toString());
        conferenceResponseDTO.setKeynoteId(conference.getKeynoteId());
        if(conference.getReviews() != null)
            conferenceResponseDTO.setReviews(conference.getReviews().stream().map(reviewMapper::toDto).toList());
        conferenceResponseDTO.setKeynote(conference.getKeynote());
        return conferenceResponseDTO;
    }

    public Conference toEntity(ConferenceRequestDTO conferenceRequestDTO) {
        Conference conference = new Conference();
        conference.setType(conferenceRequestDTO.getType());
        conference.setDate(conferenceRequestDTO.getDate());
        conference.setDuree(conferenceRequestDTO.getDuree());
        conference.setScore(conferenceRequestDTO.getScore());
        conference.setTitre(conferenceRequestDTO.getTitre());
        conference.setNombreInscrits(conferenceRequestDTO.getNombreInscrits());
        conference.setKeynoteId(conferenceRequestDTO.getKeynoteId());
        if(conferenceRequestDTO.getReviews() != null)
            conference.setReviews(conferenceRequestDTO.getReviews().stream().map(reviewMapper::toEntity).toList());
        return conference;
    }
}

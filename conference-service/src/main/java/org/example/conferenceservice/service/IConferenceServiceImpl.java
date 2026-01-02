package org.example.conferenceservice.service;

import org.example.conferenceservice.dto.ConferenceRequestDTO;
import org.example.conferenceservice.dto.ConferenceResponseDTO;
import org.example.conferenceservice.dto.ReviewRequestDTO;
import org.example.conferenceservice.entity.Conference;
import org.example.conferenceservice.entity.Review;
import org.example.conferenceservice.mapper.ConferenceMapper;
import org.example.conferenceservice.mapper.ReviewMapper;
import org.example.conferenceservice.model.Keynote;
import org.example.conferenceservice.repository.ConferenceRepository;
import org.example.conferenceservice.web.KeynoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IConferenceServiceImpl implements IConferenceService {

    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    KeynoteClient keynoteClient;
    @Autowired
    ConferenceMapper conferenceMapper;
    @Autowired
    ReviewMapper reviewMapper;

    @Override
    public List<ConferenceResponseDTO> getConferences() {
        List<Conference> conferences = conferenceRepository.findAll();
        conferences.forEach(conference -> {
            if(conference.getKeynoteId() != null) conference.setKeynote(keynoteClient.getKeynoteById(conference.getKeynoteId()));
        });
        return conferences.stream().map(conferenceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ConferenceResponseDTO getConferenceById(Long id) {
        Conference conference = conferenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Conference not found"));
        Keynote keynote = keynoteClient.getKeynoteById(conference.getKeynoteId());
        conference.setKeynote(keynote);
        return conferenceMapper.toDTO(conference);
    }

    @Override
    public void createConference(ConferenceRequestDTO conference) {
        conferenceRepository.save(conferenceMapper.toEntity(conference));
    }

    @Override
    public void updateConference(Long id, ConferenceRequestDTO conference) {
        Conference existingConference = conferenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Conference not found"));
        existingConference.setTitre(conference.getTitre());
        existingConference.setDate(conference.getDate());
        existingConference.setKeynoteId(conference.getKeynoteId());
        existingConference.setDuree(conference.getDuree());
        existingConference.setNombreInscrits(conference.getNombreInscrits());
        existingConference.setScore(conference.getScore());
        existingConference.setType(conference.getType());
        existingConference.getReviews().clear();
        if (conference.getReviews() != null) {
            conference.getReviews().stream().map(reviewMapper::toEntity).forEach(review -> {
                review.setConference(existingConference);
                existingConference.getReviews().add(review);
            });
        }
        conferenceRepository.save(existingConference);
    }

    public void patchConference(Long id, ConferenceRequestDTO updatedData) {
        Conference existing = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        // 🔹 Mise à jour partielle des champs simples (uniquement si non null)
        if (updatedData.getTitre() != null) existing.setTitre(updatedData.getTitre());
        if (updatedData.getDate() != null) existing.setDate(updatedData.getDate());
        if (updatedData.getKeynoteId() != null) existing.setKeynoteId(updatedData.getKeynoteId());
        if (updatedData.getDuree() != null) existing.setDuree(updatedData.getDuree());
        if (updatedData.getNombreInscrits() != 0) existing.setNombreInscrits(updatedData.getNombreInscrits());
        if (updatedData.getScore() != null) existing.setScore(updatedData.getScore());
        if (updatedData.getType() != null) existing.setType(updatedData.getType());

        // 🔹 Gestion partielle de la relation reviews
        if (updatedData.getReviews() != null && !updatedData.getReviews().isEmpty()) {
            // Option 1 : remplacer tout (attention, comme dans PUT)
            existing.getReviews().clear();
            updatedData.getReviews().stream().map(reviewMapper::toEntity).forEach(review -> {
                review.setConference(existing);
                existing.getReviews().add(review);
            });
        }

        conferenceRepository.save(existing);
    }

    @Override
    public void patchConferenceReviews(Long id, List<ReviewRequestDTO> reviews) {
        Conference existing = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        if(reviews != null) {
            reviews.stream().map(reviewMapper::toEntity).forEach(review -> {
                review.setConference(existing);
                existing.getReviews().add(review);
            });
        }

        conferenceRepository.save(existing);
    }

    @Override
    public void  deleteReviewFromConference(Long conferenceId, Long reviewId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        Review reviewToDelete = conference.getReviews().stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found in this conference"));

        conference.removeReview(reviewToDelete);
        conferenceRepository.save(conference);
    }


    @Override
    public void deleteConference(Long id) {
        conferenceRepository.deleteById(id);
    }
}

package org.example.conferenceservice.service;

import org.example.conferenceservice.dto.ConferenceRequestDTO;
import org.example.conferenceservice.dto.ConferenceResponseDTO;
import org.example.conferenceservice.dto.ReviewRequestDTO;

import java.util.List;

public interface IConferenceService {
    List<ConferenceResponseDTO> getConferences();
    ConferenceResponseDTO getConferenceById(Long id);
    void createConference(ConferenceRequestDTO conference);
    void updateConference(Long id, ConferenceRequestDTO conference);
    void patchConference(Long id, ConferenceRequestDTO conference);
    void patchConferenceReviews(Long id, List<ReviewRequestDTO> reviews);
    void deleteReviewFromConference(Long conferenceId, Long reviewId);
    void deleteConference(Long id);
}

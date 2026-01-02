package org.example.conferenceservice.mcp;

import org.example.conferenceservice.dto.ReviewRequestDTO;
import org.example.conferenceservice.dto.ReviewResponseDTO;
import org.example.conferenceservice.service.IConferenceService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewTools {

    private IConferenceService conferenceService;

    public ReviewTools(IConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @McpTool(description = """
            Récupère toutes les reviews d'une conférence via son identifiant conferenceId.
            Input: long conferenceId
            Output: Liste de ReviewResponseDTO [
                {
                    id: number,
                    date: date,
                    commentaire: string
                },
                ...
            ]
            """)
    public List<ReviewResponseDTO> getReviewsByConference(long conferenceId) {
        return conferenceService.getConferenceById(conferenceId).getReviews();
    }

    @McpTool(description = """
            Met à jour les reviews d'une conférence existante via son identifiant conferenceId.
            Input: long conferenceId, List<ReviewRequestDTO> [
                {
                    date: date,
                    commentaire: string
                },
                ...
            ]
            Output: Aucun
            """)
    public void updateReviewsForConference(long conferenceId, List<ReviewRequestDTO> reviews) {
        conferenceService.patchConferenceReviews(conferenceId, reviews);
    }

    @McpTool(description = """
            Supprime une review spécifique d'une conférence via ses identifiants conferenceId et reviewId.
            Input: long conferenceId, long reviewId
            Output: Aucun
            """)
    public void deleteReviewFromConference(long conferenceId, long reviewId) {
        conferenceService.deleteReviewFromConference(conferenceId, reviewId);
    }
}

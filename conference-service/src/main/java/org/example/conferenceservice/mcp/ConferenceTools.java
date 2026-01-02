package org.example.conferenceservice.mcp;

import org.example.conferenceservice.dto.ConferenceRequestDTO;
import org.example.conferenceservice.dto.ConferenceResponseDTO;
import org.example.conferenceservice.service.IConferenceService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConferenceTools {

    private IConferenceService conferenceService;

    public ConferenceTools(IConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }


    @McpTool( description = """
                    Récupère la liste de tous les conférences enregistrés.
                    Input: Aucun paramètre
                    Output: Liste de ConferenceResponseDto [
                        {
                              id : number,
                              titre : string,
                              type : TypeConference {
                                Academic,
                                commercial
                              },
                              date : date,
                              duree : number,
                              nombreInscrits : number,
                              score : number,
                              reviews : List<ReviewResponseDTO> {
                                id: number,
                                date: date,
                                commentaire : string
                              },
                              keynoteId : number,
                              keynote : Keynote{
                              id : number,
                              nom : string,
                              prenom : string,
                              email : string,
                              fonction : string
                              }
                        },
                        ...
                    ]
                    """)
    public List<ConferenceResponseDTO> getAllConferences(){
        return conferenceService.getConferences();
    }
    @McpTool(description = """
            Recupere la conference via son identifiant conferenceId.
            Input: long conferenceId
            Output: ConferenceResponseDTO  {
                              id : number,
                              titre : string,
                              type : TypeConference {
                                Academic,
                                commercial
                              },
                              date : date,
                              duree : number,
                              nombreInscrits : number,
                              score : number,
                              reviews : List<ReviewResponseDTO> {
                                id: number,
                                date: date,
                                commentaire : string
                              },
                              keynoteId : number,
                              keynote : Keynote{
                              id : number,
                              nom : string,
                              prenom : string,
                              email : string,
                              fonction : string
                              }
                        }
            """)
    public ConferenceResponseDTO getConferenceBy(long conferenceId){
        return conferenceService.getConferenceById(conferenceId);
    }

    @McpTool(description = """
            crée une conference.
            Input: ConferenceRequestDTO {
                              titre : string,
                              type : string,
                              date : date,
                              duree : number,
                              nombreInscrits : number,
                              score : number,
                              reviews : List<ReviewRequestDTO> {
                                date: date,
                                commentaire : string
                              },
                              keynoteId : number
                        }
            Output: Aucun
            """)
    public void createConference(ConferenceRequestDTO conferenceRequestDTO){
        conferenceService.createConference(conferenceRequestDTO);
    }
    @McpTool(description = """
            Met à jour une conference existante via son identifiant conferenceId.
            Input: long conferenceId, ConferenceRequestDTO {
                              titre : string,
                              type : TypeConference {
                                Academic,
                                commercial
                              },
                              date : date,
                              duree : number,
                              nombreInscrits : number,
                              score : number,
                              reviews : List<ReviewRequestDTO> {
                                date: date,
                                commentaire : string
                              },
                              keynoteId : number
                        }
            Output: Aucun
            """)
    public void updateConference(long conferenceId, ConferenceRequestDTO conferenceRequestDTO){
        conferenceService.updateConference(conferenceId, conferenceRequestDTO);
    }

    @McpTool(description = """
            supprime une conference via son identifiant conferenceId.
            Input: long conferenceId
            Output: Aucun
            """)
    public void deleteConference(long conferenceId){
        conferenceService.deleteConference(conferenceId);
    }
}

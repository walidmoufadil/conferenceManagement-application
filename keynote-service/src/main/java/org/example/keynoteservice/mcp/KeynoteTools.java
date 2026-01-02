package org.example.keynoteservice.mcp;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.service.IKeynoteService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeynoteTools {
    private IKeynoteService iKeynoteService;


    public KeynoteTools(IKeynoteService iKeynoteService) {
        this.iKeynoteService = iKeynoteService;
    }

    @McpTool(description = """
            Récupère la liste de tous les keynote speakers enregistrés.
            Input: Aucun paramètre
            Output: Liste de KeynoteResponseDTO [
                {
                    id: number,
                    nom: string,
                    prenom: string,
                    email: string,
                    fonction: string
                },
                ...
            ]
            """)
    public List<KeynoteResponseDTO> getAllKeynotes() {
        return iKeynoteService.getAllKeynotes();
    }

    @McpTool(description = """
            Récupère un keynote speaker via son identifiant keynoteId.
            Input: Long keynoteId
            Output: KeynoteResponseDTO {
                    id: number,
                    nom: string,
                    prenom: string,
                    email: string,
                    fonction: string
                }
            """)
    public KeynoteResponseDTO getKeynoteById(Long keynoteId) {
        return iKeynoteService.getKeynoteById(keynoteId);
    }

    @McpTool(description = """
            Crée un nouveau keynote speaker.
            Input: KeynoteRequestDTO {
                    nom: string,
                    prenom: string,
                    email: string,
                    fonction: string
                }
            Output: Aucun
            """)
    public void createKeynote(KeynoteRequestDTO keynoteRequestDTO) {
        iKeynoteService.createKeynote(keynoteRequestDTO);
    }

    @McpTool(description = """
            Met à jour un keynote speaker existant via son identifiant keynoteId.
            Input: Long keynoteId, KeynoteRequestDTO {
                    nom: string,
                    prenom: string,
                    email: string,
                    fonction: string
                }
            Output: Aucun
            """)
    public void updateKeynote(Long keynoteId, KeynoteRequestDTO keynoteRequestDTO) {
        iKeynoteService.updateKeynote(keynoteId, keynoteRequestDTO);
    }

    @McpTool(description = """
            Met à jour partiellement un keynote speaker via son identifiant keynoteId.
            Input: Long keynoteId, KeynoteRequestDTO {
                    nom: string (optionnel),
                    prenom: string (optionnel),
                    email: string (optionnel),
                    fonction: string (optionnel)
                }
            Output: Aucun
            """)
    public void patchKeynote(Long keynoteId, KeynoteRequestDTO keynoteRequestDTO) {
        iKeynoteService.patchKeynote(keynoteId, keynoteRequestDTO);
    }

    @McpTool(description = """
            Supprime un keynote speaker via son identifiant keynoteId.
            Input: Long keynoteId
            Output: Aucun
            """)
    public void deleteKeynote(Long keynoteId) {
        iKeynoteService.deleteKeynote(keynoteId);
    }
}

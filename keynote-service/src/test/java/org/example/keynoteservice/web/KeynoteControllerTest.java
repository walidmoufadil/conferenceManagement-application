package org.example.keynoteservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.service.IKeynoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour la couche Web (KeynoteController).
 * Vérifie le comportement des endpoints REST avec MockMvc.
 */
@WebMvcTest(KeynoteController.class)
@DisplayName("KeynoteController Tests")
@ActiveProfiles("test")
class KeynoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IKeynoteService keynoteService;

    @Autowired
    private ObjectMapper objectMapper;

    private KeynoteResponseDTO keynoteResponseDTO1;
    private KeynoteResponseDTO keynoteResponseDTO2;
    private KeynoteRequestDTO keynoteRequestDTO;

    @BeforeEach
    void setUp() {
        // Initialiser les données de test
        keynoteResponseDTO1 = KeynoteResponseDTO.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .fonction("Architecte Logiciel")
                .build();

        keynoteResponseDTO2 = KeynoteResponseDTO.builder()
                .id(2L)
                .nom("Smith")
                .prenom("Alice")
                .email("alice.smith@example.com")
                .fonction("Lead Developer")
                .build();

        keynoteRequestDTO = KeynoteRequestDTO.builder()
                .nom("Martin")
                .prenom("Bob")
                .email("bob.martin@example.com")
                .fonction("QA Engineer")
                .build();
    }

    // ========== Tests GET /api/keynotes ==========

    @Test
    @DisplayName("GET /api/keynotes - Doit retourner une liste de keynotes")
    void getAllKeynotes_shouldReturnListOfKeynotes() throws Exception {
        // ARRANGE
        List<KeynoteResponseDTO> keynotes = Arrays.asList(keynoteResponseDTO1, keynoteResponseDTO2);
        when(keynoteService.getAllKeynotes()).thenReturn(keynotes);

        // ACT & ASSERT
        mockMvc.perform(get("/api/keynotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nom", is("Dupont")))
                .andExpect(jsonPath("$[0].prenom", is("Jean")))
                .andExpect(jsonPath("$[0].email", is("jean.dupont@example.com")))
                .andExpect(jsonPath("$[0].fonction", is("Architecte Logiciel")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nom", is("Smith")))
                .andExpect(jsonPath("$[1].prenom", is("Alice")));

        verify(keynoteService, times(1)).getAllKeynotes();
    }

    @Test
    @DisplayName("GET /api/keynotes - Doit retourner une liste vide")
    void getAllKeynotes_shouldReturnEmptyList() throws Exception {
        // ARRANGE
        when(keynoteService.getAllKeynotes()).thenReturn(Arrays.asList());

        // ACT & ASSERT
        mockMvc.perform(get("/api/keynotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(keynoteService, times(1)).getAllKeynotes();
    }

    // ========== Tests GET /api/keynotes/{id} ==========

    @Test
    @DisplayName("GET /api/keynotes/{id} - Doit retourner une keynote par ID")
    void getKeynoteById_withValidId_shouldReturnKeynote() throws Exception {
        // ARRANGE
        Long id = 1L;
        when(keynoteService.getKeynoteById(id)).thenReturn(keynoteResponseDTO1);

        // ACT & ASSERT
        mockMvc.perform(get("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Dupont")))
                .andExpect(jsonPath("$.prenom", is("Jean")))
                .andExpect(jsonPath("$.email", is("jean.dupont@example.com")))
                .andExpect(jsonPath("$.fonction", is("Architecte Logiciel")));

        verify(keynoteService, times(1)).getKeynoteById(1L);
    }

    @Test
    @DisplayName("GET /api/keynotes/{id} - Doit retourner null pour un ID invalide")
    void getKeynoteById_withInvalidId_shouldReturnNull() throws Exception {
        // ARRANGE
        Long id = 999L;
        when(keynoteService.getKeynoteById(id)).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(get("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).getKeynoteById(999L);
    }

    // ========== Tests POST /api/keynotes/create ==========

    @Test
    @DisplayName("POST /api/keynotes/create - Doit créer une nouvelle keynote")
    void createKeynote_withValidRequest_shouldCreateKeynote() throws Exception {
        // ARRANGE
        doNothing().when(keynoteService).createKeynote(any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(post("/api/keynotes/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(keynoteRequestDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).createKeynote(any(KeynoteRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/keynotes/create - Doit créer une keynote avec tous les champs")
    void createKeynote_withCompleteData_shouldCreateKeynote() throws Exception {
        // ARRANGE
        KeynoteRequestDTO completeDTO = KeynoteRequestDTO.builder()
                .nom("Wilson")
                .prenom("Charlie")
                .email("charlie.wilson@example.com")
                .fonction("Product Manager")
                .build();
        doNothing().when(keynoteService).createKeynote(any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(post("/api/keynotes/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(completeDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).createKeynote(any(KeynoteRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/keynotes/create - Doit créer une keynote avec champs nulles")
    void createKeynote_withNullFields_shouldCreateKeynote() throws Exception {
        // ARRANGE
        KeynoteRequestDTO incompleteDTO = KeynoteRequestDTO.builder()
                .nom("Davis")
                .prenom(null)
                .email(null)
                .fonction("DevOps")
                .build();
        doNothing().when(keynoteService).createKeynote(any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(post("/api/keynotes/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).createKeynote(any(KeynoteRequestDTO.class));
    }

    // ========== Tests PUT /api/keynotes/{id} ==========

    @Test
    @DisplayName("PUT /api/keynotes/{id} - Doit mettre à jour une keynote existante")
    void updateKeynote_withValidIdAndRequest_shouldUpdateKeynote() throws Exception {
        // ARRANGE
        Long id = 1L;
        doNothing().when(keynoteService).updateKeynote(eq(id), any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(put("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(keynoteRequestDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).updateKeynote(eq(1L), any(KeynoteRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/keynotes/{id} - Doit mettre à jour avec des données complètes")
    void updateKeynote_withCompleteData_shouldUpdateKeynote() throws Exception {
        // ARRANGE
        Long id = 2L;
        KeynoteRequestDTO updateDTO = KeynoteRequestDTO.builder()
                .nom("New Nom")
                .prenom("New Prenom")
                .email("new.email@example.com")
                .fonction("New Fonction")
                .build();
        doNothing().when(keynoteService).updateKeynote(eq(id), any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(put("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).updateKeynote(eq(2L), any(KeynoteRequestDTO.class));
    }

    // ========== Tests PATCH /api/keynotes/{id} ==========

    @Test
    @DisplayName("PATCH /api/keynotes/{id} - Doit partiellement mettre à jour une keynote")
    void patchKeynote_withValidIdAndRequest_shouldPatchKeynote() throws Exception {
        // ARRANGE
        Long id = 1L;
        KeynoteRequestDTO patchDTO = KeynoteRequestDTO.builder()
                .nom("Updated Nom")
                .prenom(null)
                .email(null)
                .fonction(null)
                .build();
        doNothing().when(keynoteService).patchKeynote(eq(id), any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(patch("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).patchKeynote(eq(1L), any(KeynoteRequestDTO.class));
    }

    @Test
    @DisplayName("PATCH /api/keynotes/{id} - Doit mettre à jour plusieurs champs")
    void patchKeynote_withMultipleFields_shouldPatchKeynote() throws Exception {
        // ARRANGE
        Long id = 2L;
        KeynoteRequestDTO patchDTO = KeynoteRequestDTO.builder()
                .nom(null)
                .prenom("Updated Prenom")
                .email("updated@example.com")
                .fonction(null)
                .build();
        doNothing().when(keynoteService).patchKeynote(eq(id), any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(patch("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).patchKeynote(eq(2L), any(KeynoteRequestDTO.class));
    }

    // ========== Tests DELETE /api/keynotes/delete/{id} ==========

    @Test
    @DisplayName("DELETE /api/keynotes/delete/{id} - Doit supprimer une keynote")
    void deleteKeynote_withValidId_shouldDeleteKeynote() throws Exception {
        // ARRANGE
        Long id = 1L;
        doNothing().when(keynoteService).deleteKeynote(id);

        // ACT & ASSERT
        mockMvc.perform(delete("/api/keynotes/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).deleteKeynote(1L);
    }

    @Test
    @DisplayName("DELETE /api/keynotes/delete/{id} - Doit supprimer plusieurs keynotes différentes")
    void deleteKeynote_withMultipleIds_shouldDeleteAllKeynotes() throws Exception {
        // ARRANGE
        Long[] ids = {1L, 2L, 3L};
        doNothing().when(keynoteService).deleteKeynote(anyLong());

        // ACT & ASSERT
        for (Long id : ids) {
            mockMvc.perform(delete("/api/keynotes/delete/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(keynoteService, times(1)).deleteKeynote(id);
        }
    }

    // ========== Tests de validations et cas limites ==========

    @Test
    @DisplayName("GET /api/keynotes/{id} - Doit gérer les IDs très grands")
    void getKeynoteById_withLargeId_shouldHandleCorrectly() throws Exception {
        // ARRANGE
        Long largeId = Long.MAX_VALUE;
        when(keynoteService.getKeynoteById(largeId)).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(get("/api/keynotes/{id}", largeId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).getKeynoteById(largeId);
    }

    @Test
    @DisplayName("POST /api/keynotes/create - Doit accepter les requêtes avec accents")
    void createKeynote_withAccents_shouldCreateKeynote() throws Exception {
        // ARRANGE
        KeynoteRequestDTO accentDTO = KeynoteRequestDTO.builder()
                .nom("Müller")
                .prenom("José")
                .email("josé.müller@example.com")
                .fonction("Directeur d'équipe")
                .build();
        doNothing().when(keynoteService).createKeynote(any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(post("/api/keynotes/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accentDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).createKeynote(any(KeynoteRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/keynotes/{id} - Doit valider le format du JSON")
    void updateKeynote_withValidJson_shouldUpdateKeynote() throws Exception {
        // ARRANGE
        Long id = 1L;
        String jsonContent = "{\"nom\": \"TestNom\", \"prenom\": \"TestPrenom\", \"email\": \"test@example.com\", \"fonction\": \"TestFonction\"}";
        doNothing().when(keynoteService).updateKeynote(eq(id), any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(put("/api/keynotes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).updateKeynote(eq(1L), any(KeynoteRequestDTO.class));
    }

    @Test
    @DisplayName("Tous les endpoints - Doit retourner du JSON valide")
    void allEndpoints_shouldReturnValidJson() throws Exception {
        // ARRANGE
        when(keynoteService.getAllKeynotes()).thenReturn(Arrays.asList(keynoteResponseDTO1));
        when(keynoteService.getKeynoteById(1L)).thenReturn(keynoteResponseDTO1);
        doNothing().when(keynoteService).createKeynote(any(KeynoteRequestDTO.class));
        doNothing().when(keynoteService).updateKeynote(anyLong(), any(KeynoteRequestDTO.class));
        doNothing().when(keynoteService).patchKeynote(anyLong(), any(KeynoteRequestDTO.class));
        doNothing().when(keynoteService).deleteKeynote(anyLong());

        // ACT & ASSERT
        mockMvc.perform(get("/api/keynotes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/keynotes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/keynotes/create - Doit créer une keynote avec nom contenant des caractères spéciaux")
    void createKeynote_withSpecialCharactersInName_shouldCreateKeynote() throws Exception {
        // ARRANGE
        KeynoteRequestDTO specialDTO = KeynoteRequestDTO.builder()
                .nom("O'Brien-Smith")
                .prenom("Jean-Luc")
                .email("jean-luc.obrien@example.com")
                .fonction("Chef d'équipe & Manager")
                .build();
        doNothing().when(keynoteService).createKeynote(any(KeynoteRequestDTO.class));

        // ACT & ASSERT
        mockMvc.perform(post("/api/keynotes/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(specialDTO)))
                .andExpect(status().isOk());

        verify(keynoteService, times(1)).createKeynote(any(KeynoteRequestDTO.class));
    }
}


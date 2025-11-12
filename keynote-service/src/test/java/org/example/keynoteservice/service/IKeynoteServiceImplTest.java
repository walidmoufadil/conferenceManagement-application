package org.example.keynoteservice.service;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.entity.Keynote;
import org.example.keynoteservice.mapper.KeynoteMapper;
import org.example.keynoteservice.repository.KeynoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour IKeynoteServiceImpl")
class IKeynoteServiceImplTest {

    @Mock
    private KeynoteRepository keynoteRepository;

    @Mock
    private KeynoteMapper keynoteMapper;

    @InjectMocks
    private IKeynoteServiceImpl keynoteService;

    private Keynote keynote;
    private KeynoteRequestDTO keynoteRequestDTO;
    private KeynoteResponseDTO keynoteResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        keynote = Keynote.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .fonction("Architecte Logiciel")
                .build();

        keynoteRequestDTO = KeynoteRequestDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .fonction("Architecte Logiciel")
                .build();

        keynoteResponseDTO = KeynoteResponseDTO.builder()
                .id(1L)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .fonction("Architecte Logiciel")
                .build();
    }

    @Test
    @DisplayName("Devrait récupérer tous les keynotes avec succès")
    void testGetAllKeynotes_Success() {
        // Arrange
        Keynote keynote2 = Keynote.builder()
                .id(2L)
                .nom("Martin")
                .prenom("Marie")
                .email("marie.martin@example.com")
                .fonction("Lead Developer")
                .build();

        KeynoteResponseDTO keynoteResponseDTO2 = KeynoteResponseDTO.builder()
                .id(2L)
                .nom("Martin")
                .prenom("Marie")
                .email("marie.martin@example.com")
                .fonction("Lead Developer")
                .build();

        List<Keynote> keynoteList = Arrays.asList(keynote, keynote2);

        when(keynoteRepository.findAll()).thenReturn(keynoteList);
        when(keynoteMapper.toKeynoteResponseDTO(keynote)).thenReturn(keynoteResponseDTO);
        when(keynoteMapper.toKeynoteResponseDTO(keynote2)).thenReturn(keynoteResponseDTO2);

        // Act
        List<KeynoteResponseDTO> result = keynoteService.getAllKeynotes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dupont", result.get(0).getNom());
        assertEquals("Martin", result.get(1).getNom());
        verify(keynoteRepository, times(1)).findAll();
        verify(keynoteMapper, times(2)).toKeynoteResponseDTO(any());
    }

    @Test
    @DisplayName("Devrait retourner une liste vide quand aucun keynote n'existe")
    void testGetAllKeynotes_EmptyList() {
        // Arrange
        when(keynoteRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<KeynoteResponseDTO> result = keynoteService.getAllKeynotes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(keynoteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un keynote par ID avec succès")
    void testGetKeynoteById_Success() {
        // Arrange
        when(keynoteRepository.findById(1L)).thenReturn(Optional.of(keynote));
        when(keynoteMapper.toKeynoteResponseDTO(keynote)).thenReturn(keynoteResponseDTO);

        // Act
        KeynoteResponseDTO result = keynoteService.getKeynoteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dupont", result.getNom());
        assertEquals("Jean", result.getPrenom());
        verify(keynoteRepository, times(1)).findById(1L);
        verify(keynoteMapper, times(1)).toKeynoteResponseDTO(keynote);
    }

    @Test
    @DisplayName("Devrait lever une exception quand le keynote n'existe pas")
    void testGetKeynoteById_NotFound() {
        // Arrange
        when(keynoteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> keynoteService.getKeynoteById(999L));
        verify(keynoteRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Devrait créer un keynote avec succès")
    void testCreateKeynote_Success() {
        // Arrange
        when(keynoteMapper.toEntity(keynoteRequestDTO)).thenReturn(keynote);
        when(keynoteRepository.save(any(Keynote.class))).thenReturn(keynote);

        // Act
        keynoteService.createKeynote(keynoteRequestDTO);

        // Assert
        verify(keynoteMapper, times(1)).toEntity(keynoteRequestDTO);
        verify(keynoteRepository, times(1)).save(any(Keynote.class));
    }

    @Test
    @DisplayName("Devrait mettre à jour un keynote avec succès")
    void testUpdateKeynote_Success() {
        // Arrange
        KeynoteRequestDTO updateRequest = KeynoteRequestDTO.builder()
                .nom("DupontMaj")
                .prenom("JeanMaj")
                .email("jean.maj@example.com")
                .fonction("Senior Architect")
                .build();

        when(keynoteRepository.findById(1L)).thenReturn(Optional.of(keynote));
        when(keynoteRepository.save(any(Keynote.class))).thenReturn(keynote);

        // Act
        keynoteService.updateKeynote(1L, updateRequest);

        // Assert
        verify(keynoteRepository, times(1)).findById(1L);
        verify(keynoteRepository, times(1)).save(any(Keynote.class));
        assertEquals("DupontMaj", keynote.getNom());
        assertEquals("JeanMaj", keynote.getPrenom());
        assertEquals("jean.maj@example.com", keynote.getEmail());
        assertEquals("Senior Architect", keynote.getFonction());
    }

    @Test
    @DisplayName("Devrait lever une exception lors de la mise à jour d'un keynote inexistant")
    void testUpdateKeynote_NotFound() {
        // Arrange
        when(keynoteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> keynoteService.updateKeynote(999L, keynoteRequestDTO));
        verify(keynoteRepository, times(1)).findById(999L);
        verify(keynoteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Devrait patcher un keynote avec tous les champs")
    void testPatchKeynote_AllFields() {
        // Arrange
        KeynoteRequestDTO patchRequest = KeynoteRequestDTO.builder()
                .nom("NouveauNom")
                .prenom("NouveauPrenom")
                .email("nouveau@example.com")
                .fonction("Nouvelle Fonction")
                .build();

        when(keynoteRepository.findById(1L)).thenReturn(Optional.of(keynote));
        when(keynoteRepository.save(any(Keynote.class))).thenReturn(keynote);

        // Act
        keynoteService.patchKeynote(1L, patchRequest);

        // Assert
        verify(keynoteRepository, times(1)).findById(1L);
        verify(keynoteRepository, times(1)).save(any(Keynote.class));
        assertEquals("NouveauNom", keynote.getNom());
        assertEquals("NouveauPrenom", keynote.getPrenom());
        assertEquals("nouveau@example.com", keynote.getEmail());
        assertEquals("Nouvelle Fonction", keynote.getFonction());
    }

    @Test
    @DisplayName("Devrait patcher un keynote avec seulement le nom")
    void testPatchKeynote_PartialFields() {
        // Arrange
        KeynoteRequestDTO patchRequest = KeynoteRequestDTO.builder()
                .nom("NouveauNom")
                .prenom(null)
                .email(null)
                .fonction(null)
                .build();

        String originalPrenom = keynote.getPrenom();
        String originalEmail = keynote.getEmail();
        String originalFonction = keynote.getFonction();

        when(keynoteRepository.findById(1L)).thenReturn(Optional.of(keynote));
        when(keynoteRepository.save(any(Keynote.class))).thenReturn(keynote);

        // Act
        keynoteService.patchKeynote(1L, patchRequest);

        // Assert
        verify(keynoteRepository, times(1)).findById(1L);
        verify(keynoteRepository, times(1)).save(any(Keynote.class));
        assertEquals("NouveauNom", keynote.getNom());
        assertEquals(originalPrenom, keynote.getPrenom());
        assertEquals(originalEmail, keynote.getEmail());
        assertEquals(originalFonction, keynote.getFonction());
    }

    @Test
    @DisplayName("Devrait lever une exception lors du patch d'un keynote inexistant")
    void testPatchKeynote_NotFound() {
        // Arrange
        when(keynoteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> keynoteService.patchKeynote(999L, keynoteRequestDTO));
        verify(keynoteRepository, times(1)).findById(999L);
        verify(keynoteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Devrait supprimer un keynote avec succès")
    void testDeleteKeynote_Success() {
        // Arrange
        doNothing().when(keynoteRepository).deleteById(1L);

        // Act
        keynoteService.deleteKeynote(1L);

        // Assert
        verify(keynoteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Devrait gérer la suppression d'un keynote inexistant")
    void testDeleteKeynote_NotFound() {
        // Arrange
        doNothing().when(keynoteRepository).deleteById(999L);

        // Act & Assert - Ne devrait pas lever d'exception
        assertDoesNotThrow(() -> keynoteService.deleteKeynote(999L));
        verify(keynoteRepository, times(1)).deleteById(999L);
    }
}


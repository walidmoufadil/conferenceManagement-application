package org.example.keynoteservice.mapper;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.entity.Keynote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests unitaires pour la couche Mapper (KeynoteMapper).
 * Vérifie la conversion correcte entre entités et DTOs.
 */
@DisplayName("KeynoteMapper Tests")
class KeynoteMapperTest {

    private KeynoteMapper keynoteMapper;

    @BeforeEach
    void setUp() {
        keynoteMapper = new KeynoteMapper();
    }

    // ========== Tests toEntity() ==========

    @Test
    @DisplayName("Doit convertir KeynoteRequestDTO en Keynote entity")
    void toEntity_withValidRequestDTO_shouldMapAllFields() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .fonction("Architecte Logiciel")
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(keynote)
                .isNotNull()
                .satisfies(k -> {
                    assertThat(k.getNom()).isEqualTo("Dupont");
                    assertThat(k.getPrenom()).isEqualTo("Jean");
                    assertThat(k.getEmail()).isEqualTo("jean.dupont@example.com");
                    assertThat(k.getFonction()).isEqualTo("Architecte Logiciel");
                    assertThat(k.getId()).isNull(); // L'ID n'est pas défini dans le DTO
                });
    }

    @Test
    @DisplayName("Doit gérer les valeurs nulles dans KeynoteRequestDTO")
    void toEntity_withNullFields_shouldHandleNullValues() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .nom("Martin")
                .prenom(null)
                .email(null)
                .fonction("Manager")
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(keynote)
                .isNotNull()
                .satisfies(k -> {
                    assertThat(k.getNom()).isEqualTo("Martin");
                    assertThat(k.getPrenom()).isNull();
                    assertThat(k.getEmail()).isNull();
                    assertThat(k.getFonction()).isEqualTo("Manager");
                });
    }

    @Test
    @DisplayName("Doit gérer un DTO RequestDTO entièrement vide")
    void toEntity_withAllNullFields_shouldCreateEntity() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(keynote)
                .isNotNull()
                .satisfies(k -> {
                    assertThat(k.getId()).isNull();
                    assertThat(k.getNom()).isNull();
                    assertThat(k.getPrenom()).isNull();
                    assertThat(k.getEmail()).isNull();
                    assertThat(k.getFonction()).isNull();
                });
    }

    // ========== Tests toKeynoteResponseDTO() ==========

    @Test
    @DisplayName("Doit convertir Keynote entity en KeynoteResponseDTO")
    void toKeynoteResponseDTO_withValidEntity_shouldMapAllFields() {
        // ARRANGE
        Keynote keynote = Keynote.builder()
                .id(1L)
                .nom("Smith")
                .prenom("Alice")
                .email("alice.smith@example.com")
                .fonction("Lead Developer")
                .build();

        // ACT
        KeynoteResponseDTO responseDTO = keynoteMapper.toKeynoteResponseDTO(keynote);

        // ASSERT
        assertThat(responseDTO)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(1L);
                    assertThat(dto.getNom()).isEqualTo("Smith");
                    assertThat(dto.getPrenom()).isEqualTo("Alice");
                    assertThat(dto.getEmail()).isEqualTo("alice.smith@example.com");
                    assertThat(dto.getFonction()).isEqualTo("Lead Developer");
                });
    }

    @Test
    @DisplayName("Doit convertir Keynote entity sans ID")
    void toKeynoteResponseDTO_withoutId_shouldMapAllFields() {
        // ARRANGE
        Keynote keynote = Keynote.builder()
                .id(null)
                .nom("Johnson")
                .prenom("Bob")
                .email("bob.johnson@example.com")
                .fonction("QA Engineer")
                .build();

        // ACT
        KeynoteResponseDTO responseDTO = keynoteMapper.toKeynoteResponseDTO(keynote);

        // ASSERT
        assertThat(responseDTO)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isNull();
                    assertThat(dto.getNom()).isEqualTo("Johnson");
                    assertThat(dto.getPrenom()).isEqualTo("Bob");
                    assertThat(dto.getEmail()).isEqualTo("bob.johnson@example.com");
                    assertThat(dto.getFonction()).isEqualTo("QA Engineer");
                });
    }

    @Test
    @DisplayName("Doit gérer les valeurs nulles dans Keynote entity")
    void toKeynoteResponseDTO_withNullFields_shouldHandleNullValues() {
        // ARRANGE
        Keynote keynote = Keynote.builder()
                .id(2L)
                .nom("Davis")
                .prenom(null)
                .email(null)
                .fonction("DevOps")
                .build();

        // ACT
        KeynoteResponseDTO responseDTO = keynoteMapper.toKeynoteResponseDTO(keynote);

        // ASSERT
        assertThat(responseDTO)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(2L);
                    assertThat(dto.getNom()).isEqualTo("Davis");
                    assertThat(dto.getPrenom()).isNull();
                    assertThat(dto.getEmail()).isNull();
                    assertThat(dto.getFonction()).isEqualTo("DevOps");
                });
    }

    // ========== Tests de cycle complet ==========

    @Test
    @DisplayName("Doit convertir RequestDTO -> Entity -> ResponseDTO avec succès")
    void fullCycle_fromRequestDTOToResponseDTO_shouldPreserveData() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .nom("Wilson")
                .prenom("Charlie")
                .email("charlie.wilson@example.com")
                .fonction("Product Manager")
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);
        keynote.setId(5L); // Simuler une sauvegarde en base de données
        KeynoteResponseDTO responseDTO = keynoteMapper.toKeynoteResponseDTO(keynote);

        // ASSERT
        assertThat(responseDTO)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(5L);
                    assertThat(dto.getNom()).isEqualTo("Wilson");
                    assertThat(dto.getPrenom()).isEqualTo("Charlie");
                    assertThat(dto.getEmail()).isEqualTo("charlie.wilson@example.com");
                    assertThat(dto.getFonction()).isEqualTo("Product Manager");
                });
    }

    // ========== Tests de cas limites ==========

    @Test
    @DisplayName("Doit gérer des valeurs avec espaces blancs")
    void toEntity_withWhitespaceValues_shouldPreserveWhitespace() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .nom("  Blanc  ")
                .prenom("  Test  ")
                .email("  test@example.com  ")
                .fonction("  Fonction  ")
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(keynote)
                .isNotNull()
                .satisfies(k -> {
                    assertThat(k.getNom()).isEqualTo("  Blanc  ");
                    assertThat(k.getPrenom()).isEqualTo("  Test  ");
                    assertThat(k.getEmail()).isEqualTo("  test@example.com  ");
                    assertThat(k.getFonction()).isEqualTo("  Fonction  ");
                });
    }

    @Test
    @DisplayName("Doit gérer des strings vides")
    void toEntity_withEmptyStrings_shouldHandleEmptyValues() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .nom("")
                .prenom("")
                .email("")
                .fonction("")
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(keynote)
                .isNotNull()
                .satisfies(k -> {
                    assertThat(k.getNom()).isEmpty();
                    assertThat(k.getPrenom()).isEmpty();
                    assertThat(k.getEmail()).isEmpty();
                    assertThat(k.getFonction()).isEmpty();
                });
    }

    @Test
    @DisplayName("Doit gérer des IDs différents en ResponseDTO")
    void toKeynoteResponseDTO_withDifferentIds_shouldPreserveId() {
        // ARRANGE
        Long[] ids = {1L, 100L, 999999L};

        for (Long id : ids) {
            Keynote keynote = Keynote.builder()
                    .id(id)
                    .nom("Test")
                    .prenom("User")
                    .email("test@example.com")
                    .fonction("Tester")
                    .build();

            // ACT
            KeynoteResponseDTO responseDTO = keynoteMapper.toKeynoteResponseDTO(keynote);

            // ASSERT
            assertThat(responseDTO.getId()).isEqualTo(id);
        }
    }

    @Test
    @DisplayName("Doit gérer des caractères spéciaux et accents")
    void toEntity_withSpecialCharacters_shouldPreserveCharacters() {
        // ARRANGE
        KeynoteRequestDTO requestDTO = KeynoteRequestDTO.builder()
                .nom("Müller")
                .prenom("José")
                .email("josé.müller@example.com")
                .fonction("Directeur d'équipe")
                .build();

        // ACT
        Keynote keynote = keynoteMapper.toEntity(requestDTO);

        // ASSERT
        assertThat(keynote)
                .isNotNull()
                .satisfies(k -> {
                    assertThat(k.getNom()).isEqualTo("Müller");
                    assertThat(k.getPrenom()).isEqualTo("José");
                    assertThat(k.getEmail()).isEqualTo("josé.müller@example.com");
                    assertThat(k.getFonction()).isEqualTo("Directeur d'équipe");
                });
    }
}


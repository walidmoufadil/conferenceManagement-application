package org.example.keynoteservice.service;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.entity.Keynote;

import java.util.List;

public interface IKeynoteService {
    List<KeynoteResponseDTO> getAllKeynotes();
    KeynoteResponseDTO getKeynoteById(Long id);
    void createKeynote(KeynoteRequestDTO keynote);
    void updateKeynote(Long id, KeynoteRequestDTO keynote);
    void patchKeynote(Long id, KeynoteRequestDTO keynote);
    void deleteKeynote(Long id);
}

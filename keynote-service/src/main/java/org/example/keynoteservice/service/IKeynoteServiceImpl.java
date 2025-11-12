package org.example.keynoteservice.service;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.entity.Keynote;
import org.example.keynoteservice.mapper.KeynoteMapper;
import org.example.keynoteservice.repository.KeynoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IKeynoteServiceImpl implements IKeynoteService {
    @Autowired
    private KeynoteRepository keynoteRepository;
    @Autowired
    private KeynoteMapper keynoteMapper;

    @Override
    public List<KeynoteResponseDTO> getAllKeynotes() {
        return keynoteRepository.findAll().stream().map(keynoteMapper::toKeynoteResponseDTO).toList();
    }

    @Override
    public KeynoteResponseDTO getKeynoteById(Long id) {
        Keynote keynote = keynoteRepository.findById(id).orElseThrow(() -> new RuntimeException("Keynote not found"));
        return keynoteMapper.toKeynoteResponseDTO(keynote);
    }

    @Override
    public void createKeynote(KeynoteRequestDTO keynote) {
        keynoteRepository.save(keynoteMapper.toEntity(keynote));
    }

    @Override
    public void updateKeynote(Long id, KeynoteRequestDTO keynote) {
    Keynote existingKeynote = keynoteRepository.findById(id).orElseThrow(() -> new RuntimeException("Keynote not found"));
    existingKeynote.setNom(keynote.getNom());
    existingKeynote.setPrenom(keynote.getPrenom());
    existingKeynote.setEmail(keynote.getEmail());
    existingKeynote.setFonction(keynote.getFonction());
    keynoteRepository.save(existingKeynote);
    }

    @Override
    public void patchKeynote(Long id, KeynoteRequestDTO keynote) {
        Keynote existingKeynote = keynoteRepository.findById(id).orElseThrow(() -> new RuntimeException("Keynote not found"));
        //if(keynote.getConferences() != null) existingKeynote.setConferences(keynote.getConferences());
        if(keynote.getNom() != null) existingKeynote.setNom(keynote.getNom());
        if(keynote.getPrenom() != null) existingKeynote.setPrenom(keynote.getPrenom());
        if(keynote.getEmail() != null) existingKeynote.setEmail(keynote.getEmail());
        if(keynote.getFonction() != null) existingKeynote.setFonction(keynote.getFonction());
        keynoteRepository.save(existingKeynote);
    }

    @Override
    public void deleteKeynote(Long id) {
    keynoteRepository.deleteById(id);
    }
}

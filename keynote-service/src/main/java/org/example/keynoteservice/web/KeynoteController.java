package org.example.keynoteservice.web;

import org.example.keynoteservice.dto.KeynoteRequestDTO;
import org.example.keynoteservice.dto.KeynoteResponseDTO;
import org.example.keynoteservice.entity.Keynote;
import org.example.keynoteservice.service.IKeynoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keynotes")
public class KeynoteController {
    @Autowired
    private IKeynoteService keynoteService;

    @GetMapping()
    public List<KeynoteResponseDTO> getKeynotes() {
       return keynoteService.getAllKeynotes();
    }
    @GetMapping("{id}")
    public KeynoteResponseDTO getKeynoteById(@PathVariable Long id) {
       return keynoteService.getKeynoteById(id);
    }
    @PostMapping("create")
    public void createKeynote(@RequestBody KeynoteRequestDTO keynote) {
       keynoteService.createKeynote(keynote);
    }
    @PutMapping("{id}")
    public void updateKeynote(@PathVariable Long id, @RequestBody KeynoteRequestDTO keynote) {
       keynoteService.updateKeynote(id, keynote);
    }
    @PatchMapping("{id}")
    public void patchKeynote(@PathVariable Long id, @RequestBody KeynoteRequestDTO keynote) {
       keynoteService.patchKeynote(id, keynote);
    }
    @DeleteMapping("delete/{id}")
    public void deleteKeynote(@PathVariable Long id) {
       keynoteService.deleteKeynote(id);
    }
}

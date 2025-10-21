package org.example.conferenceservice.web;

import org.example.conferenceservice.entity.Conference;
import org.example.conferenceservice.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ConferenceController {
    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private KeynoteClient keynoteClient;

    @GetMapping("conferences")
    public List<Conference> getAllConferences() {
        List<Conference> conferences = conferenceRepository.findAll();
        conferences.forEach(conference -> {
            conference.setKeynote(keynoteClient.getKeynote(conference.getKeynoteId()));
        });
        return conferences;
    }
}

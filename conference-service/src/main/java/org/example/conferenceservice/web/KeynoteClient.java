package org.example.conferenceservice.web;

import org.example.conferenceservice.model.Keynote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "keynote-service")
public interface KeynoteClient {
    @GetMapping("/api/keynotes/{id}")
    public Keynote getKeynoteById(@PathVariable Long id);
}

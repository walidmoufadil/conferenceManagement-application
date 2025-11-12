package org.example.keynoteservice.repository;

import org.example.keynoteservice.entity.Keynote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeynoteRepository extends JpaRepository<Keynote, Long> {
}

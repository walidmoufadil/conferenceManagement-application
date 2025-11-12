package org.example.conferenceservice.repository;

import org.example.conferenceservice.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}

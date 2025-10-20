package org.example.conferenceservice.repository;

import org.example.conferenceservice.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}

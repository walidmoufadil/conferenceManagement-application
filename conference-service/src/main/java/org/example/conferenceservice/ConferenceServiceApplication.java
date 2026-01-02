package org.example.conferenceservice;

import org.example.conferenceservice.entity.Conference;
import org.example.conferenceservice.entity.Review;
import org.example.conferenceservice.entity.TypeConference;
import org.example.conferenceservice.mcp.ConferenceTools;
import org.example.conferenceservice.mcp.ReviewTools;
import org.example.conferenceservice.repository.ConferenceRepository;
import org.example.conferenceservice.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class ConferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceServiceApplication.class, args);
    }

//    @Bean
//    MethodToolCallbackProvider getmethodToolCallbackProvider(ConferenceTools conferenceTools, ReviewTools reviewTools) {
//        return MethodToolCallbackProvider.builder()
//                .toolObjects(conferenceTools, reviewTools)
//                .build();
//    }
    @Bean
    CommandLineRunner commandLineRunner(ConferenceRepository conferenceRepository, ReviewRepository repository, ReviewRepository reviewRepository) {
        return args -> {
            Conference conference1 = Conference.builder()
                    .titre("Spring Boot Conference")
                    .date(new Date())
                    .duree(120.0)
                    .type(TypeConference.Academic)
                    .keynoteId(1L)
                    .nombreInscrits(21)
                    .build();
            Conference conference2 = Conference.builder()
                    .titre("Microservices Architecture")
                    .date(new Date())
                    .duree(90.0)
                    .type(TypeConference.Academic)
                    .keynoteId(2L)
                    .nombreInscrits(35)
                    .build();
            conferenceRepository.saveAll(List.of(conference1, conference2));

            Review review1 = Review.builder()
                    .commentaire("Good Conference")
                    .conference(conference1)
                    .build();
            Review review2 = Review.builder()
                    .commentaire("Excellent Content")
                    .conference(conference2)
                    .build();
            reviewRepository.saveAll(List.of(review1, review2));


        };
    }
}

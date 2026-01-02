package org.example.keynoteservice;

import org.example.keynoteservice.entity.Keynote;
import org.example.keynoteservice.mcp.KeynoteTools;
import org.example.keynoteservice.repository.KeynoteRepository;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class KeynoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeynoteServiceApplication.class, args);
    }

//    @Bean
//    MethodToolCallbackProvider getmethodToolCallbackProvider(KeynoteTools keynoteTools) {
//        return MethodToolCallbackProvider.builder()
//                .toolObjects(keynoteTools)
//                .build();
//    }
    @Profile("!test")
    @Bean
    CommandLineRunner commandLineRunner(KeynoteRepository keynoteRepository) {
        return args -> {
          keynoteRepository.saveAll(List.of(
                  Keynote.builder()
                          .nom("Doe")
                          .prenom("John")
                          .email("john@gmail.com")
                          .fonction("Professor")
                          .build()
                    ,
                  Keynote.builder()
                          .nom("Cartner")
                          .prenom("Mari")
                          .email("maria@gmail.com")
                          .fonction("Professor")
                          .build() ));
        };
    }
}

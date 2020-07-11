package com.magnojr.pollservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnojr.pollservice.domain.Poll;
import com.magnojr.pollservice.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@Configuration
public class PopulateInitialDataConfig {

    @Value("classpath:polls.json")
    private Resource initialStateResource;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PollRepository pollRepository;

    @Bean
    public void populateData() throws IOException {

        List<Poll> polls = objectMapper.readValue(initialStateResource.getInputStream(), new TypeReference<>() {
        });

        pollRepository.saveAll(polls);

    }

}

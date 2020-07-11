package com.magnojr.pollservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnojr.pollservice.domain.Poll;
import com.magnojr.pollservice.dtos.TitlePaginationSearchDTO;
import com.magnojr.pollservice.helpers.CustomPollPageDTO;
import com.magnojr.pollservice.repositories.PollRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PollControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PollController pollController;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:polls.json")
    private Resource initialStateResource;

    @Autowired
    private PollRepository pollRepository;


    @BeforeEach
    void loadData() throws IOException {
        List<Poll> polls = objectMapper.readValue(initialStateResource.getInputStream(), new TypeReference<>() {
        });

        pollRepository.saveAll(polls);
    }

    @Test
    void itShouldListAllPollWhenNoFilterIsProvided() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/polls"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

        List<Poll> pollList = toPollList(response.getContentAsString());
        Assertions.assertThat(pollList.size()).isEqualTo(36);
    }

    @Test
    void itShouldListAllPollsCreatedAfterCertainDateWhenReceivesAValidDate() throws Exception {
        final String fromDate = "2017-01-27";

        MockHttpServletResponse response = mockMvc.perform(
                get("/polls?fromDate=" + fromDate))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

        List<Poll> pollList = toPollList(response.getContentAsString());

        long initiatedFrom = LocalDate.parse(fromDate)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        Assertions.assertThat(pollList.size()).isEqualTo(2);
        pollList.forEach(poll ->
                Assertions.assertThat(poll.getInitiated()).isGreaterThan(initiatedFrom)
        );
    }

    @Test
    void itShouldListAllPollsCreatedByUserWhenReceiveAValidUser() throws Exception {

        final String createdBy = "John Doe";

        MockHttpServletResponse response = mockMvc.perform(
                get("/polls?createdBy=" + createdBy))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

        List<Poll> pollList = toPollList(response.getContentAsString());

        Assertions.assertThat(pollList.size()).isEqualTo(36);
        pollList.forEach(poll ->
                Assertions.assertThat(poll.getInitiator().getName()).isEqualTo(createdBy)
        );


    }

    @Test
    void itShouldSearchPollsByTitleWhenReceiveEmptyTitle() throws Exception {

        int page = 0;
        int size = 10;
        int totalElements = 36;

        TitlePaginationSearchDTO searchDTO = TitlePaginationSearchDTO.builder()
                .page(page)
                .size(size)
                .title("")
                .build();


        MockHttpServletResponse response = mockMvc.perform(
                post("/polls/search")
                        .contentType("application/json")
                        .content(toJson(searchDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

        CustomPollPageDTO pollPage = toPollPage(response.getContentAsString());

        Assertions.assertThat(pollPage.getSize()).isEqualTo(size);
        Assertions.assertThat(pollPage.getNumber()).isEqualTo(page);
        Assertions.assertThat(pollPage.getTotalElements()).isEqualTo(totalElements);

    }

    @Test
    void itShouldSearchPollsByTitleWhenReceiveATitle() throws Exception {
        int page = 0;
        int size = 10;
        int totalElements = 3;
        String partialText = "the";

        TitlePaginationSearchDTO searchDTO = TitlePaginationSearchDTO.builder()
                .page(page)
                .size(size)
                .title(partialText)
                .build();


        MockHttpServletResponse response = mockMvc.perform(
                post("/polls/search")
                        .contentType("application/json")
                        .content(toJson(searchDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

        CustomPollPageDTO pollPage = toPollPage(response.getContentAsString());

        Assertions.assertThat(pollPage.getSize()).isEqualTo(size);
        Assertions.assertThat(pollPage.getNumber()).isEqualTo(page);
        Assertions.assertThat(pollPage.getTotalElements()).isEqualTo(totalElements);

        List<Poll> pollList = pollPage.getContent();
        Assertions.assertThat(pollList.size()).isEqualTo(totalElements);
        pollList.forEach(poll ->
                Assertions.assertThat(poll.getTitle()).contains(partialText)
        );


    }

    private String toJson(TitlePaginationSearchDTO searchDTO) {
        try {
            return objectMapper.writeValueAsString(searchDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Poll> toPollList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private CustomPollPageDTO toPollPage(String json) {
        try {
            return objectMapper.readValue(json, CustomPollPageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
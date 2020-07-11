package com.magnojr.pollservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnojr.pollservice.dtos.TitlePaginationSearchDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PollControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PollController pollController;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void itShouldListAllPollsCreatedAfterCertainDateWhenReceivesAValidDate() throws Exception {
        final String fromDate = "2017-01-27";

        MockHttpServletResponse response = mockMvc.perform(
                get("/polls?fromDate=" + fromDate))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

    }

    @Test
    void itShouldListAllPollsCreatedByUserWhenReceiveAValidUser() throws Exception {

        final String createdBy = "John Doe";

        MockHttpServletResponse response = mockMvc.perform(
                get("/polls?createdBy=" + createdBy))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();


    }

    @Test
    void itShouldSearchPollsByTitleWHenReceiveEmptyTitle() throws Exception {
        TitlePaginationSearchDTO searchDTO = TitlePaginationSearchDTO.builder()
                .page(0)
                .size(10)
                .title("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                post("/polls/search")
                        .contentType("application/json")
                        .content(toJson(searchDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();

    }

    private String toJson(TitlePaginationSearchDTO searchDTO) {
        try {
            return objectMapper.writeValueAsString(searchDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.magnojr.pollservice.controllers;


import com.magnojr.pollservice.domain.Poll;
import com.magnojr.pollservice.dtos.TitlePaginationSearchDTO;
import com.magnojr.pollservice.services.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;

    @GetMapping(value = "/polls")
    public List<Poll> listPollsByCreatedBy(@RequestParam Optional<String> createdBy,
                                           @RequestParam
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                   Optional<LocalDate> fromDate) {

        if (createdBy.isPresent()) {
            return pollService.listCreatedBy(createdBy.get());
        }

        if (fromDate.isPresent()) {
            return pollService.listFromDate(fromDate.get());
        }

        return pollService.listAllPolls();
    }

    @PostMapping(value = "/polls/search", consumes = "application/json", produces = "application/json")
    public Page<Poll> searchByTitle(@RequestBody @Validated TitlePaginationSearchDTO titlePaginationSearchDTO) {
        return pollService.searchByTitle(titlePaginationSearchDTO);
    }

}

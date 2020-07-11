package com.magnojr.pollservice.services;

import com.magnojr.pollservice.domain.Poll;
import com.magnojr.pollservice.dtos.TitlePaginationSearchDTO;
import com.magnojr.pollservice.repositories.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;

    public List<Poll> listAllPolls() {
        return pollRepository.findAll();
    }

    public Page<Poll> searchByTitle(final TitlePaginationSearchDTO pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(),
                pagination.getSize(),
                Sort.by("title").ascending()
        );
        return pollRepository.findByTitleContainingIgnoreCase(pagination.getTitle(), pageable);
    }

    public List<Poll> listCreatedBy(final String createdBy) {

        return pollRepository.findByInitiatorName(createdBy);

    }

    public List<Poll> listFromDate(final LocalDate fromDate) {

        long initiatedFrom = fromDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        return pollRepository.findByInitiatedGreaterThanOrderByInitiatedDesc(initiatedFrom);
    }
}

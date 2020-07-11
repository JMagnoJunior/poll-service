package com.magnojr.pollservice.repositories;

import com.magnojr.pollservice.domain.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PollRepository extends MongoRepository<Poll, String>, PagingAndSortingRepository<Poll, String> {


    List<Poll> findByInitiatorName(String name);

    List<Poll> findByInitiatedGreaterThanOrderByInitiatedDesc(long initiated);

    Page<Poll> findByTitleContaining(String title, Pageable pageable);

}

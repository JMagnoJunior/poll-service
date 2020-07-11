package com.magnojr.pollservice.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnojr.pollservice.domain.Poll;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties
public class CustomPollPageDTO {
    /*
        This class defines a custom deserialization for Page<Poll>, so Object Mapper can
        convert it easily.
     */
    private int number;
    private int size;
    private int totalPages;
    private int numberOfElements;
    private long totalElements;
    private List<Poll> content;
}

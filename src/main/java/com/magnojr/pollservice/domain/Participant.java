package com.magnojr.pollservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    private long id;
    private String name;
    private List<Integer> preferences;
}

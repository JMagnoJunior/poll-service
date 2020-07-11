package com.magnojr.pollservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Initiator {

    private String name;
    private String email;
    private String notify;

}

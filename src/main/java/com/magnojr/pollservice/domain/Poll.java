package com.magnojr.pollservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
    @Id
    private String id;
    private String adminKey;
    private long latestChange;
    private long initiated;
    private int participantsCount;
    private int inviteesCount;
    private String type;
    private boolean hidden;
    private String preferencesType;
    private String state;
    private String locale;
    private String title;
    private Initiator initiator;
    private List<Option> options;
    private String optionsHash;
    private List<Participant> participants;
    private List<String> invitees;
    private String device;
    private String levels;
}
package com.magnojr.pollservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TitlePaginationSearchDTO {

    private String title;
    private int page;
    private int size;

}

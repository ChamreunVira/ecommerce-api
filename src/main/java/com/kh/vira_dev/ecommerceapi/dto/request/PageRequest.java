package com.kh.vira_dev.ecommerceapi.dto.request;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private boolean ascending = true;

    public Pageable toPageable() {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return org.springframework.data.domain.PageRequest.of(page , size ,sort);
    }

}

package com.uca.labo3hyrule.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
}


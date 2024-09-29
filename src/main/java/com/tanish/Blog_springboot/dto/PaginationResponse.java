package com.tanish.Blog_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponse<T>{
    private Integer statusCode;
    private String message;
    private Integer pageNumber;
    private Integer pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
    private T data;
}

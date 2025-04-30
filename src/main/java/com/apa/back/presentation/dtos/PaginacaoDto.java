package com.apa.back.presentation.dtos;

import java.util.List;

public record PaginacaoDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}


package com.apa.back.presentation.dtos.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PaginacaoDto<T>(
        @Schema(description = "Lista de itens da página")
        List<T> content,

        @Schema(description = "Número da página atual (base zero)", example = "0")
        int page,

        @Schema(description = "Tamanho da página", example = "20")
        int size,

        @Schema(description = "Total de elementos disponíveis", example = "100")
        long totalElements,

        @Schema(description = "Total de páginas disponíveis", example = "5")
        int totalPages
) {}

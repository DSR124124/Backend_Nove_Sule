package com.nove.sule.backend_nove_sule.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO genérico para respuestas de la API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;
    private String error;
    private Integer code;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponseDTO<T> success(T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String error) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .error(error)
                .build();
    }

    public static <T> ApiResponseDTO<T> error(String error, Integer code) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .error(error)
                .code(code)
                .build();
    }
}

package com.example.spring_security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
@JsonProperty("status_code")
private Integer statusCode;
private String message;
private Object data;

    public ApiResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

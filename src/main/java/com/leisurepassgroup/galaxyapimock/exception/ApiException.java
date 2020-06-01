package com.leisurepassgroup.galaxyapimock.exception;

import com.leisurepassgroup.galaxyapimock.model.response.error.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    ErrorResponse errorResponse;
}

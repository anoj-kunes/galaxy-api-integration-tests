package com.leisurepassgroup.galaxyapimock.exception.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisurepassgroup.galaxyapimock.exception.ApiException;
import com.leisurepassgroup.galaxyapimock.model.response.error.ErrorResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ApiErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || clientHttpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        var response = IOUtils.toString(clientHttpResponse.getBody(), "UTF-8");
        var objectMapper = new ObjectMapper();
        ErrorResponse apiErrorDto = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(response, new TypeReference<ErrorResponse>() {});

        throw new ApiException(apiErrorDto);
    }
}

package com.leisurepassgroup.galaxyapimock.model.response;

import lombok.Value;

import java.time.ZonedDateTime;

@Value
public class TicketUsageResponse {
    String ticketNumber;
    String productCode;
    Integer quantity;
    String statusText;
    Integer uses;
    ZonedDateTime useTime;
    String operationId;
    Integer errorCode;
    String errorMessage;
    Integer httpStatusCode;
    Boolean hasError;
}

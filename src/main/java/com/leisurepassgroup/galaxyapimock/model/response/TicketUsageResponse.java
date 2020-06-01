package com.leisurepassgroup.galaxyapimock.model.response;

import lombok.Value;

@Value
public class TicketUsageResponse {
    String ticketNumber;
    String productCode;
    Integer quantity;
    Integer status;
    String statusText;
    Integer uses;
    String useTime;
    String operationId;
    Integer errorCode;
    String errorMessage;
    Integer httpStatusCode;
    Boolean hasError;
}

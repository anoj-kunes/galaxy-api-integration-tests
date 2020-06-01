package com.leisurepassgroup.galaxyapimock.model.response;

import lombok.Value;

@Value
public class TicketActivationResponse {
    String ticketNumber;
    String productCode;
    String supplierName;
    Integer resultCode;
    String correlationId;
    TicketActivationErrorResponse errors;
}

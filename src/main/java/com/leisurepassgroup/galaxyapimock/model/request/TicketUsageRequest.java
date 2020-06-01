package com.leisurepassgroup.galaxyapimock.model.request;

import lombok.Value;

@Value
public class TicketUsageRequest {
    String productCode;
    Integer quantity;
    Integer status;
    String useTime;
    String operationId;
}

package com.leisurepassgroup.galaxyapimock.service;

import com.leisurepassgroup.galaxyapimock.model.request.TicketActivationRequest;
import com.leisurepassgroup.galaxyapimock.model.response.TicketActivationResponse;
import com.leisurepassgroup.galaxyapimock.model.response.TicketUsageResponse;

public interface TicketClientService {
    TicketActivationResponse activateTicket(Integer ticketNumber, TicketActivationRequest request);
    TicketActivationResponse deactivateTicket(Integer ticketNumber, TicketActivationRequest request);
    TicketUsageResponse ticketUsage(Integer ticketNumber, TicketUsageResponse request);
}

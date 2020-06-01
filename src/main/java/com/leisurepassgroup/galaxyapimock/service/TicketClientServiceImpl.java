package com.leisurepassgroup.galaxyapimock.service;

import com.leisurepassgroup.galaxyapimock.exception.api.ApiErrorHandler;
import com.leisurepassgroup.galaxyapimock.model.request.TicketActivationRequest;
import com.leisurepassgroup.galaxyapimock.model.response.TicketActivationResponse;
import com.leisurepassgroup.galaxyapimock.model.response.TicketUsageResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@Service
public class TicketClientServiceImpl implements TicketClientService {
    private static final String TICKET_URL = "/api/tickets/{ticketNumber}/activations";
    private static final String TICKET_USAGES_URL = "/api/tickets/{ticketNumber}/usage";
    private static final String URL = "http://localhost:9088";
    private final RestTemplate restTemplate;

    public TicketClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new ApiErrorHandler());
    }

    public TicketActivationResponse activateTicket(Integer ticketNumber, TicketActivationRequest request) {
        var httpEntity = new HttpEntity<>(request, getHeaders());
        var response = restTemplate.exchange(URL + TICKET_URL, POST, httpEntity, TicketActivationResponse.class, ticketNumber).getBody();
        return response;
    }

    public TicketActivationResponse deactivateTicket(Integer ticketNumber, TicketActivationRequest request) {
        var httpEntity = new HttpEntity<>(request, getHeaders());
        var response = restTemplate.exchange(URL + TICKET_URL, DELETE, httpEntity, TicketActivationResponse.class, ticketNumber).getBody();
        return response;
    }

    public TicketUsageResponse ticketUsage(Integer ticketNumber, TicketUsageResponse request) {
        var httpEntity = new HttpEntity<>(request, getHeaders());
        var response = restTemplate.exchange(URL + TICKET_USAGES_URL, POST, httpEntity, TicketUsageResponse.class, ticketNumber).getBody();
        return response;
    }

    private HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.set("x-api-key", "abcdef12345");

        return headers;
    }
}

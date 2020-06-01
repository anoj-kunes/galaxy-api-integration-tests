package com.leisurepassgroup.galaxyapimock.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisurepassgroup.galaxyapimock.exception.ApiException;
import com.leisurepassgroup.galaxyapimock.service.TicketClientService;
import com.leisurepassgroup.galaxyapimock.service.TicketClientServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.model.Header;
import org.springframework.web.client.RestTemplate;

import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.API_KEY_HEADERS;
import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.createDefaultExpectations;
import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.createDefaultRequest;
import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.createDefaultResponse;
import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.forbiddenAccess;
import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.ticketCannotBeActivatedError;
import static com.leisurepassgroup.galaxyapimock.expectation.ActivationTicketExpectation.unauthorizedAccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.verify.VerificationTimes.exactly;

public class ActivateTicketTests extends BaseTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate = getRestTemplate();
    private TicketClientService ticketClientService = new TicketClientServiceImpl(restTemplate);

    @BeforeAll
    public static void setupExpectations() throws JsonProcessingException {
        createDefaultExpectations(mockServer);
    }

    @Test
    public void activateTicket_ticketActivatedSuccessfully_methodCalledAndReturns() throws JsonProcessingException {
        var output = ticketClientService.activateTicket(101, createDefaultRequest());
        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/101/activations"),
                exactly(1)
        );

        assertEquals(createDefaultResponse(101, null), output);
    }

    @Test
    public void activateTicket_ticketCannotBeActivated_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.activateTicket(102, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/102/activations"),
                exactly(1)
        );

        assertEquals(ticketCannotBeActivatedError(), error.getErrorResponse());
    }

    @Test
    public void activateTicket_forbiddenAccess_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.activateTicket(103, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/103/activations"),
                exactly(1)
        );

        assertEquals(forbiddenAccess(), error.getErrorResponse());
    }

    @Test
    public void activateTicket_unauthorizedAccess_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.activateTicket(104, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/104/activations"),
                exactly(1)
        );

        assertEquals(unauthorizedAccess(), error.getErrorResponse());
    }
}

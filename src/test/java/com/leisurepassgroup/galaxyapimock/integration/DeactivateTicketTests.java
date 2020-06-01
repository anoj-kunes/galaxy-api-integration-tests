package com.leisurepassgroup.galaxyapimock.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisurepassgroup.galaxyapimock.exception.ApiException;
import com.leisurepassgroup.galaxyapimock.service.TicketClientService;
import com.leisurepassgroup.galaxyapimock.service.TicketClientServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.API_KEY_HEADERS;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.createDefaultRequest;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.createDefaultResponse;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.createDefaultTicketDeactivationExpectations;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.forbiddenAccess;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.ticketCannotBeActivatedError;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketExpectation.unauthorizedAccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.verify.VerificationTimes.exactly;

public class DeactivateTicketTests extends BaseTests {


    @BeforeAll
    public static void setupExpectations() throws JsonProcessingException {
        createDefaultTicketDeactivationExpectations(mockServer);
    }

    @Test
    public void deactivateTicket_ticketDeactivatedSuccessfully_methodCalledAndReturns() throws JsonProcessingException {
        var output = ticketClientService.deactivateTicket(101, createDefaultRequest());
        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withMethod("DELETE")
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/101/activations"),
                exactly(1)
        );

        assertEquals(createDefaultResponse(101, null), output);
    }

    @Test
    public void deactivateTicket_ticketCannotBeActivated_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.deactivateTicket(102, createDefaultRequest()));

        mockServer.verify(
                request()
                        .withMethod("DELETE")
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/102/activations"),
                exactly(1)
        );

        assertEquals(ticketCannotBeActivatedError(), error.getErrorResponse());
    }

    @Test
    public void deactivateTicket_forbiddenAccess_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.deactivateTicket(103, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withMethod("DELETE")
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/103/activations"),
                exactly(1)
        );

        assertEquals(forbiddenAccess(), error.getErrorResponse());
    }

    @Test
    public void deactivateTicket_unauthorizedAccess_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.deactivateTicket(104, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withMethod("DELETE")
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/104/activations"),
                exactly(1)
        );

        assertEquals(unauthorizedAccess(), error.getErrorResponse());
    }
}

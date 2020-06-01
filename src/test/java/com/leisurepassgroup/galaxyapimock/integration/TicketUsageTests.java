package com.leisurepassgroup.galaxyapimock.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leisurepassgroup.galaxyapimock.exception.ApiException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.API_KEY_HEADERS;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.internalServerError;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.unauthorizedAccess;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.conflict;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.createDefaultRequest;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.createDefaultResponse;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.createDefaultTicketUsagesExpectations;
import static com.leisurepassgroup.galaxyapimock.expectation.TicketUsageExpectation.requestNotValid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.verify.VerificationTimes.exactly;

public class TicketUsageTests extends BaseTests {
    @BeforeAll
    public static void setupExpectations() throws JsonProcessingException {
        createDefaultTicketUsagesExpectations(mockServer);
    }

    @Test
    public void ticketUsage_ticketUsageUpdatedSuccessfully_methodCalledAndReturns() throws JsonProcessingException {
        var output = ticketClientService.ticketUsage(101, createDefaultRequest());
        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withMethod("POST")
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/101/usage"),
                exactly(1)
        );

        assertEquals(createDefaultResponse(101), output);
    }

    @Test
    public void ticketUsage_requestNotValid_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.ticketUsage(102, createDefaultRequest()));

        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withMethod("POST")
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/102/usage"),
                exactly(1)
        );

        assertEquals(requestNotValid(), error.getErrorResponse());
    }

    @Test
    public void ticketUsage_unauthorizedAccess_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.ticketUsage(104, createDefaultRequest()));

        mockServer.verify(
                request()
                        .withHeader(API_KEY_HEADERS)
                        .withMethod("POST")
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/104/usage"),
                exactly(1)
        );

        assertEquals(unauthorizedAccess(), error.getErrorResponse());
    }

    @Test
    public void ticketUsage_internalServerError_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.ticketUsage(105, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withMethod("POST")
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/105/usage"),
                exactly(1)
        );

        assertEquals(internalServerError(), error.getErrorResponse());
    }

    @Test
    public void ticketUsage_conflict_throwsApiException() throws JsonProcessingException {
        var error = assertThrows(ApiException.class, () -> ticketClientService.ticketUsage(106, createDefaultRequest()));
        mockServer.verify(
                request()
                        .withMethod("POST")
                        .withHeader(API_KEY_HEADERS)
                        .withBody(objectMapper.writeValueAsString(createDefaultRequest()))
                        .withPath("/api/tickets/106/usage"),
                exactly(1)
        );

        assertEquals(conflict(), error.getErrorResponse());
    }
}

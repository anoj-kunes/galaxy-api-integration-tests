package com.leisurepassgroup.galaxyapimock.expectation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisurepassgroup.galaxyapimock.model.request.TicketUsageRequest;
import com.leisurepassgroup.galaxyapimock.model.response.TicketUsageResponse;
import com.leisurepassgroup.galaxyapimock.model.response.error.ErrorResponse;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.MediaType;
import java.util.UUID;

import static java.lang.String.format;
import static org.mockserver.matchers.Times.once;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class TicketUsageExpectation {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TICKET_USAGES_URL = "/api/tickets/%s/usage";
    public static final Header API_KEY_HEADERS = new Header("x-api-key", "abcdef12345");

    public static final String PRODUCT_CODE = "PC01";
    public static final Integer QUANTITY = 11;
    public static final String OPERATION_ID = UUID.randomUUID().toString();
    public static final String CORRELATION_ID = UUID.randomUUID().toString();

    public static void createDefaultTicketUsagesExpectations(ClientAndServer mockServer) throws JsonProcessingException {
        // success
        ticketUsages(
                mockServer,
                API_KEY_HEADERS,
                101,
                createDefaultRequest(),
                createDefaultResponse(101),
                201
        );

        // fail - Bad Request - Error Code 3
        ticketUsages(
                mockServer,
                API_KEY_HEADERS,
                102,
                createDefaultRequest(),
                requestNotValid(),
                400
        );

        // fail - Unauthorized - Error Code 5
        ticketUsages(
                mockServer,
                API_KEY_HEADERS,
                104,
                createDefaultRequest(),
                unauthorizedAccess(),
                401
        );

        // fail - internalServerError - Error Code 500
        ticketUsages(
                mockServer,
                API_KEY_HEADERS,
                105,
                createDefaultRequest(),
                internalServerError(),
                500
        );

        // fail - conflict - Error Code 4
        ticketUsages(
                mockServer,
                API_KEY_HEADERS,
                106,
                createDefaultRequest(),
                conflict(),
                409
        );
    }

    private static void ticketUsages(ClientAndServer mockServer, Header header, Integer ticketNumber, TicketUsageRequest request, Object response, Integer statusCode) throws JsonProcessingException {
        mockServer.when(
                request()
                        .withHeader(header)
                        .withMethod("POST")
                        .withPath(format(TICKET_USAGES_URL, ticketNumber))
                        .withBody(objectMapper.writeValueAsString(request)),
                once()
        ).respond(
                response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withStatusCode(statusCode)
                        .withBody(objectMapper.writeValueAsString(response))
        );
    }

    public static TicketUsageResponse createDefaultResponse(Integer ticketNumber) {
        return new TicketUsageResponse(
                ticketNumber.toString(),
                PRODUCT_CODE,
                QUANTITY,
                200,
                "SUCCESS",
                1,
                "2020-06-01T10:11:02.994Z",
                OPERATION_ID,
                null,
                null,
                null,
                false
        );
    }

    public static TicketUsageRequest createDefaultRequest() {
        return new TicketUsageRequest(
                PRODUCT_CODE,
                QUANTITY,
                400,
                "2020-06-01T10:11:02.994Z",
                OPERATION_ID
        );
    }

    public static ErrorResponse requestNotValid() {
        return new ErrorResponse(400, 3, "Request not Valid", CORRELATION_ID);
    }

    public static ErrorResponse unauthorizedAccess() {
        return new ErrorResponse(401, 5, "Authorization has been denied", CORRELATION_ID);
    }

    public static ErrorResponse internalServerError() {
        return new ErrorResponse(500, 999999, "Unexpected exception", CORRELATION_ID);
    }

    public static ErrorResponse conflict() {
        return new ErrorResponse(409, 4, "Record version conflict", CORRELATION_ID);
    }
}

package com.leisurepassgroup.galaxyapimock.expectation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisurepassgroup.galaxyapimock.model.request.TicketActivationRequest;
import com.leisurepassgroup.galaxyapimock.model.response.TicketActivationErrorResponse;
import com.leisurepassgroup.galaxyapimock.model.response.TicketActivationResponse;
import com.leisurepassgroup.galaxyapimock.model.response.error.ErrorResponse;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.MediaType;

import java.util.UUID;

import static java.lang.String.format;
import static org.mockserver.matchers.Times.once;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class TicketExpectation {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TICKET_URL = "/api/tickets/%s/activations";
    public static final Header API_KEY_HEADERS = new Header("x-api-key", "abcdef12345");

    public static final String PRODUCT_CODE = "PC01";
    public static final String SUPPLIER_NAME = "LPG_INTEGRATION_TEST";
    public static final String CORRELATION_ID = UUID.randomUUID().toString();

    public static void createDefaultTicketActivationExpectations(ClientAndServer mockServer) throws JsonProcessingException {
        // success
        activateTicket(
                mockServer,
                API_KEY_HEADERS,
                101,
                createDefaultRequest(),
                createDefaultResponse(101, null),
                201
        );

        // fail - Bad Request - Error Code 1420
        activateTicket(
                mockServer,
                API_KEY_HEADERS,
                102,
                createDefaultRequest(),
                ticketCannotBeActivatedError(),
                400
        );

        // fail - Forbidden - Error Code 906
        activateTicket(
                mockServer,
                API_KEY_HEADERS,
                103,
                createDefaultRequest(),
                forbiddenAccess(),
                401
        );

        // fail - Unauthorized - Error Code 5
        activateTicket(
                mockServer,
                API_KEY_HEADERS,
                104,
                createDefaultRequest(),
                unauthorizedAccess(),
                401
        );

        // fail - NotFound - Error Code 200
        activateTicket(
                mockServer,
                API_KEY_HEADERS,
                105,
                createDefaultRequest(),
                productNotFound(),
                403
        );

        // fail - conflict - Error Code 4
        activateTicket(
                mockServer,
                API_KEY_HEADERS,
                106,
                createDefaultRequest(),
                conflict(),
                403
        );
    }

    public static void createDefaultTicketDeactivationExpectations(ClientAndServer mockServer) throws JsonProcessingException {
        // success
        deactivateTicket(
                mockServer,
                API_KEY_HEADERS,
                101,
                createDefaultRequest(),
                createDefaultResponse(101, null),
                201
        );

        // fail - Bad Request - Error Code 1420
        deactivateTicket(
                mockServer,
                API_KEY_HEADERS,
                102,
                createDefaultRequest(),
                ticketCannotBeActivatedError(),
                400
        );

        // fail - Forbidden - Error Code 906
        deactivateTicket(
                mockServer,
                API_KEY_HEADERS,
                103,
                createDefaultRequest(),
                forbiddenAccess(),
                401
        );

        // fail - Unauthorized - Error Code 5
        deactivateTicket(
                mockServer,
                API_KEY_HEADERS,
                104,
                createDefaultRequest(),
                unauthorizedAccess(),
                401
        );

        // fail - NotFound - Error Code 200
        deactivateTicket(
                mockServer,
                API_KEY_HEADERS,
                105,
                createDefaultRequest(),
                productNotFound(),
                403
        );

        // fail - conflict - Error Code 4
        deactivateTicket(
                mockServer,
                API_KEY_HEADERS,
                106,
                createDefaultRequest(),
                conflict(),
                409
        );
    }

    private static void deactivateTicket(ClientAndServer mockServer, Header header, Integer ticketNumber, TicketActivationRequest request, Object response, Integer statusCode) throws JsonProcessingException {
        mockServer.when(
                request()
                        .withHeader(header)
                        .withMethod("DELETE")
                        .withPath(format(TICKET_URL, ticketNumber))
                        .withBody(objectMapper.writeValueAsString(request)),
                once()
        ).respond(
                response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withStatusCode(statusCode)
                        .withBody(objectMapper.writeValueAsString(response))
        );
    }

    private static void activateTicket(ClientAndServer mockServer, Header header, Integer ticketNumber, TicketActivationRequest request, Object response, Integer statusCode) throws JsonProcessingException {
        mockServer.when(
                request()
                        .withHeader(header)
                        .withMethod("POST")
                        .withPath(format(TICKET_URL, ticketNumber))
                        .withBody(objectMapper.writeValueAsString(request)),
                once()
        ).respond(
                response()
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withStatusCode(statusCode)
                        .withBody(objectMapper.writeValueAsString(response))
        );
    }

    public static TicketActivationResponse createDefaultResponse(Integer ticketNumber, TicketActivationErrorResponse errors) {
        return new TicketActivationResponse(
                ticketNumber.toString(),
                PRODUCT_CODE,
                SUPPLIER_NAME,
                1,
                CORRELATION_ID,
                errors
        );
    }

    public static TicketActivationRequest createDefaultRequest() {
        return new TicketActivationRequest(PRODUCT_CODE);
    }

    public static ErrorResponse ticketCannotBeActivatedError() {
        return new ErrorResponse(400, 1420, "Ticket Cannot be activated because it has already been deactivated", CORRELATION_ID);
    }

    public static ErrorResponse unauthorizedAccess() {
        return new ErrorResponse(401, 5, "Authorization has been denied", CORRELATION_ID);
    }

    public static ErrorResponse forbiddenAccess() {
        return new ErrorResponse(403, 906, "All activity on this channel and its related products are disabled.", CORRELATION_ID);
    }

    public static ErrorResponse productNotFound() {
        return new ErrorResponse(404, 200, "The Product was not found in this Channel", CORRELATION_ID);
    }

    public static ErrorResponse conflict() {
        return new ErrorResponse(409, 4, "Record version conflict", CORRELATION_ID);
    }
}

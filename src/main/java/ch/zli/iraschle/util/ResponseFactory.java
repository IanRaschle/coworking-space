package ch.zli.iraschle.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.Response.Status.*;


public class ResponseFactory {
    public static final Response NO_CONTENT = Response.status(Status.NO_CONTENT).build();

    public static final Response APPLICATION_USER_ID_NOT_EXISTING = Response.status(BAD_REQUEST).entity("There is no ApplicationUser with this id").build();
    public static final Response BOOKING_ID_NOT_EXISTING = Response.status(BAD_REQUEST).entity("There is no Booking with this id").build();
    public static final Response INVALID_CREDENTIALS = Response.status(UNAUTHORIZED).entity("The email or password was wrong").build();

    public static Response createResponse(Status httpstatus, Object body) {
        return Response
                .status(httpstatus)
                .entity(body)
                .build();
    }
}

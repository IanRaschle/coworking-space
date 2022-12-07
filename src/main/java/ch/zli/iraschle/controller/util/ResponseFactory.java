package ch.zli.iraschle.controller.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class ResponseFactory {
    public static final Response NO_CONTENT = Response.status(Status.NO_CONTENT).build();

    public static Response createResponse(Status httpstatus, Object body) {
        return Response
                .status(httpstatus)
                .entity(body)
                .build();
    }
}

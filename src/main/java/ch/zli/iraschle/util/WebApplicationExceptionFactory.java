package ch.zli.iraschle.util;

import javax.ws.rs.WebApplicationException;

import static ch.zli.iraschle.util.ResponseFactory.APPLICATION_USER_ID_NOT_EXISTING;

public class WebApplicationExceptionFactory {
    public static WebApplicationException NO_USER_WITH_ID = new WebApplicationException("There is no ApplicationUser with this id", APPLICATION_USER_ID_NOT_EXISTING);
    public static WebApplicationException INVALID_CREDENTIALS = new WebApplicationException("", ResponseFactory.INVALID_CREDENTIALS);
}

package ch.zli.iraschle.util;

import javax.ws.rs.WebApplicationException;

import static ch.zli.iraschle.util.ResponseFactory.*;

public class WebApplicationExceptionFactory {
    public static WebApplicationException NO_USER_WITH_ID = new WebApplicationException("There is no ApplicationUser with this id", APPLICATION_USER_ID_NOT_EXISTING);
    public static WebApplicationException NO_USER_WITH_EMAIL = new WebApplicationException("There is no ApplicationUser with this email", APPLICATION_USER_EMAIL_NOT_EXISTING);
    public static WebApplicationException DUPLICATED_EMAIL = new WebApplicationException("There is already an ApplicationUser with this email", ResponseFactory.DUPLICATED_EMAIL);


    public static WebApplicationException INVALID_CREDENTIALS = new WebApplicationException("The email or password was wrong", ResponseFactory.INVALID_CREDENTIALS);
    public static WebApplicationException NOT_YOUR_BOOKING = new WebApplicationException("You are only allowed to manipulate your own bookings", ResponseFactory.NOT_YOUR_BOOKING);

    public static WebApplicationException NO_BOOKING_WITH_ID = new WebApplicationException("There is no Booking with this id", BOOKING_ID_NOT_EXISTING);
    public static WebApplicationException NOT_VALID_BOOKING = new WebApplicationException("The submitted booking is not valid or null", ResponseFactory.NOT_VALID_BOOKING);
}

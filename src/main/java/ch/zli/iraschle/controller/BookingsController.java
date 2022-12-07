package ch.zli.iraschle.controller;

import ch.zli.iraschle.model.booking.BookingEntity;
import ch.zli.iraschle.model.booking.BookingState;
import ch.zli.iraschle.service.BookingsService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static ch.zli.iraschle.controller.util.ResponseFactory.NO_CONTENT;
import static ch.zli.iraschle.controller.util.ResponseFactory.createResponse;
import static javax.ws.rs.core.Response.Status.ACCEPTED;
import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/bookings")
public class BookingsController {
  @Inject
  BookingsService bookingsService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  @Operation(summary = "Get all bookings", description = "Get all existing bookings")
  public Response getBookings() {
    List<BookingEntity> bookings = bookingsService.getBookings();
    if (bookings.isEmpty()) {
      return NO_CONTENT;
    }
    return createResponse(ACCEPTED, bookings);
  }

  @GET
  @Path("/sorted")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  @Operation(summary = "Get all bookings sorted", description = "Get all existing bookings sorted after the booking's date")
  public Response getBookingsSorted() {
    List<BookingEntity> bookingsSorted = bookingsService.getBookingsSorted();
    if (bookingsSorted.isEmpty()) {
      return NO_CONTENT;
    }
    return createResponse(ACCEPTED, bookingsSorted);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Create a new booking", description = "Creates a new booking with the submitted data")
  public Response createBooking(@Valid BookingEntity bookingEntity) {
    BookingEntity booking = bookingsService.createBooking(bookingEntity);
    return createResponse(CREATED, booking);
  }

  @DELETE
  @Path("/{id}")
  @Produces
  @Consumes
  @Operation(summary = "Delete or reverse a booking", description = "The admin user can delete any booking and the member can only reverse his own bookings")
  public void removeBooking(@PathParam("id") long id) {
    bookingsService.deleteBooking(id);
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Update a booking", description = "Updates the existing booking with the given data")
  public BookingEntity updateBooking(@Valid BookingEntity bookingEntity) {
    return bookingsService.updateBooking(bookingEntity);
  }

  @GET
  @Path("/state/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes
  @Operation(summary = "Get the booking state", description = "Get the state of a booking with the given id")
  public String getBookingState(@PathParam("id") long id) {
    return bookingsService.getBookingStateOf(id).name();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  @Operation(summary = "Get the booking", description = "Get the booking with the given id")
  public BookingEntity getBooking(@PathParam("id") long id) {
    return bookingsService.getBooking(id);
  }

  @POST
  @Path("/accept/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  public BookingEntity acceptBooking(@PathParam("id") long id) {
    return bookingsService.changeBookingState(id, BookingState.ACCEPTED);
  }

  @POST
  @Path("/deny/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes
  public BookingEntity denyBooking(@PathParam("id") long id) {
    return bookingsService.changeBookingState(id, BookingState.DENIED);
  }
}

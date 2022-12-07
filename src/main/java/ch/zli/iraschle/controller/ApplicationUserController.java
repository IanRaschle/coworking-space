package ch.zli.iraschle.controller;

import ch.zli.iraschle.util.ApplicationUserDto;
import ch.zli.iraschle.model.user.ApplicationUserEntity;
import ch.zli.iraschle.service.ApplicationUserService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static ch.zli.iraschle.util.PasswortHashing.hashPassword;
import static ch.zli.iraschle.util.ResponseFactory.NO_CONTENT;
import static ch.zli.iraschle.util.ResponseFactory.createResponse;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.*;

@Path("/users")
public class ApplicationUserController {
  @Inject
  ApplicationUserService applicationUserService;

  @GET
  @Produces(APPLICATION_JSON)
  @Consumes
  @Operation(summary = "Get all ApplicationUsers", description = "Get all existing ApplicationUsers")
  public Response getApplicationUsers() {
    List<ApplicationUserEntity> applicationUsers = applicationUserService.getApplicationUsers();
    if (applicationUsers.isEmpty()) {
      return NO_CONTENT;
    }
    return createResponse(OK, applicationUsers);
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  @Operation(summary = "Create a new ApplicationUser", description = "Creates a new ApplicationUser with the submitted data")
  public Response createApplicationUser(@Valid ApplicationUserDto applicationUserDto) {
    ApplicationUserEntity applicationUser = applicationUserService.createApplicationUser(applicationUserDto.toNewApplicationUser());
    return createResponse(CREATED, applicationUser);
  }

  @DELETE
  @Path("/{id}")
  @Produces
  @Consumes
  @Operation(summary = "Delete an ApplicationUser", description = "Deletes the ApplicationUser with the given id")
  public void removeApplicationUser(@PathParam("id") long id) {
    applicationUserService.deleteApplicationUser(id);
  }

  @PUT
  @Produces(APPLICATION_JSON)
  @Consumes(APPLICATION_JSON)
  @Operation(summary = "Update an ApplicationUser", description = "Updates the existing ApplicationUser with the given data")
  public Response updateApplicationUser(@Valid ApplicationUserDto applicationUserDto) {
    ApplicationUserEntity applicationUser = applicationUserService.updateApplicationUser(applicationUserDto.toApplicationUser());
    return createResponse(OK, applicationUser);
  }

  @GET
  @Path("/{id}")
  @Produces(APPLICATION_JSON)
  @Consumes
  @Operation(summary = "Get the booking", description = "Get the booking with the given id")
  public ApplicationUserEntity getApplicationUser(@PathParam("id") long id) {
    return applicationUserService.getApplicationUser(id);
  }

  @PATCH
  @Path("/{id}/email")
  @Produces(APPLICATION_JSON)
  @Consumes(TEXT_PLAIN)
  public ApplicationUserEntity updateEmail(@PathParam("id") long id, @NotBlank String newEmail) {
    return applicationUserService.changeEmailOfApplicationUser(id, newEmail);
  }

  @PATCH
  @Path("/{id}/password")
  @Produces(APPLICATION_JSON)
  @Consumes(TEXT_PLAIN)
  public ApplicationUserEntity updatePassword(@PathParam("id") long id, @NotBlank String newPassword) {
    return applicationUserService.changePasswordOfApplicationUser(id, hashPassword(newPassword));
  }
}

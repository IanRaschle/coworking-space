package ch.zli.iraschle.controller;

import ch.zli.iraschle.model.session.Credentials;
import ch.zli.iraschle.service.SessionService;
import ch.zli.iraschle.util.ApplicationUserDto;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestResponse;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@PermitAll
public class SessionController {
  @Inject
  SessionService sessionService;

  @POST
  @Path("sign-up")
  @Produces(APPLICATION_JSON)
  @Consumes(APPLICATION_JSON)
  @Operation(summary = "Create new Account", description = "Create a new Account (ApplicationUser) with the given data")
  public Response getApplicationUsers(@Valid ApplicationUserDto applicationUserDto) {
    String token = sessionService.sign_up(applicationUserDto.toNewApplicationUser());
    return RestResponse.ResponseBuilder.ok("", MediaType.APPLICATION_JSON)
            .header("Authorization", token)
            .build().toResponse();
  }

  @POST
  @Path("sign-in")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  @Operation(summary = "Sign in", description = "Sign in with the given credentials")
  public Response createApplicationUser(@Valid Credentials credentials) {
    String token = sessionService.sign_in(credentials);
    return RestResponse.ResponseBuilder.ok("", MediaType.APPLICATION_JSON)
            .header("Authorization", token)
            .build().toResponse();
  }
}

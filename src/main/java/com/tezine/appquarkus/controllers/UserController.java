package com.tezine.appquarkus.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.tezine.appquarkus.codes.Logger;
import com.tezine.appquarkus.codes.UserNotFoundException;
import com.tezine.appquarkus.entities.User;
import com.tezine.appquarkus.services.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path("/v1/users")
@Tag(name = "UserController", description = "User operations.")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// @SecurityScheme(securitySchemeName = "Basic Auth", type =
// SecuritySchemeType.HTTP, scheme = "basic")
public class UserController {

    @Inject
    UserService userService;

    @GET
    @Path("/get-users")
    // @RolesAllowed({"USER", "ADMIN"})
    @Operation(summary = "Gets users", description = "Lists all available users")
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    public Response getUsers() {
        try {
            return Response.ok(userService.getAllUsers()).build();
        } catch (Exception e) {
            Logger.logError(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    // @RolesAllowed({"USER", "ADMIN"})
     @Path("/get-user/{id}")
    @Operation(summary = "Gets a user", description = "Retrieves a user by id")
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)))
    @APIResponse(responseCode = "404", description = "Not Found")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    public Response getUser(@PathParam("id") int id) throws UserNotFoundException {
        try {
            var user = userService.getUserById(id);
            if (user == null)
                return Response.status(Status.NOT_FOUND).build();
            return Response.ok().build();
        } catch (Exception e) {
            Logger.logError(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/add-user")
    @PermitAll
    @Operation(summary = "Adds a user", description = "Creates a user and persists into database")
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(type = SchemaType.INTEGER)))
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    public Response addUser(@Valid User user) {
        try {
            var id = userService.saveUser(user);
            return Response.ok(id).build();
        } catch (Exception e) {
            Logger.logError(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    // @RolesAllowed("ADMIN")
    @Path("/update-user/{id}")
    @Operation(summary = "Updates a user", description = "Updates an existing user by id")
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)))
    @APIResponse(responseCode = "400", description = "Bad Request")
    @APIResponse(responseCode = "404", description = "Not Found")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    public Response updateUser(@PathParam("id") int id, @Valid User user) {
        try {
            var result = userService.updateUser(id, user);
            if (result == null)
                return Response.status(Status.NOT_FOUND).build();
            return Response.ok(result).build();
        } catch (Exception e) {
            Logger.logError(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    // @RolesAllowed("ADMIN")
    @Path("/delete-user-by-id/{id}")
    @Operation(summary = "Deletes a user", description = "Deletes a user by id")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "404", description = "Not Found")
    @APIResponse(responseCode = "500", description = "Internal Server Error")
    public Response deleteUser(@PathParam("id") int id) {
        try {
            var ok = userService.deleteUser(id);
            if (ok)
                return Response.ok().build();
            else
                return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
            Logger.logError(e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

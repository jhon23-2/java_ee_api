package org.example.rest;

import org.example.model.UserModel;
import org.example.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON) //Input
@Consumes(MediaType.APPLICATION_JSON) //Output
public class UserController {

    @Inject
    private UserService userService;


    @GET
    @Path("/welcome")
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() {
        return "Welcome to the User API! Im Glober developer :)";
    }


    @POST
    public Response create(UserModel user) {
        userService.createUser(user);
        return Response.status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    @GET
    public Response getAll() {
        return Response.status(Response.Status.OK)
                .entity(userService.findAllUsers())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        UserModel user = userService.findByUserId(id);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK)
                .entity(user)
                .build();
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        userService.deleteUser(id);
    }
}

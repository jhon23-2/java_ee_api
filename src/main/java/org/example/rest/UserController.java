package org.example.rest;

import org.example.model.UserModel;
import org.example.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON) //Input
@Consumes(MediaType.APPLICATION_JSON) //Output
public class UserController {

    @Inject
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/welcome")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response welcomeMessage() {
        return Response.status(Response.Status.OK).entity("Thanks Jesus i feel grateful with you!!!").build();
    }

    @POST
    public Response create(@Valid UserModel user){
        UserModel userModel = this.userService.create(user);
        URI location = this.uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(userModel.getEmail()))
                .build();

        return Response.created(location)
                .status(Response.Status.CREATED)
                .entity(userModel).build();
    }


    @GET
    public Response findAll(){
        return Response.status(Response.Status.OK)
                .entity(this.userService.findAll())
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK)
                .entity(this.userService.findById(id))
                .build();
    }

    @GET
    public Response findEmail(@QueryParam("email") String email) {
        UserModel userModel = this.userService.findByEmail(email);
        return Response.status(Response.Status.OK)
                .entity(userModel)
                .build();
    }


    @PUT
    @Path("/{id}")
    public Response update(UserModel user, @PathParam("id") Long id){
        return Response.status(Response.Status.OK)
                .entity(this.userService.updateUser(user, id))
                .build();
    }


    @DELETE
    @Path("/{id}")
    public Response delete( @PathParam("id") Long id) {
        this.userService.delete(id);
        return Response.status(Response.Status.NO_CONTENT)
                .build();
    }

}

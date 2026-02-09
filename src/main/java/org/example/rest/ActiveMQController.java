package org.example.rest;


import org.example.jms.FirstQueue;
import org.example.model.MessageModel;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/activemq")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActiveMQController {

    @Inject
    private FirstQueue firstQueue;

    @POST
    public Response avtiveMQ(@Valid  MessageModel messageModel) {
        this.firstQueue.createInitialQueue(messageModel.getMessage());
        return Response.status(Response.Status.OK)
                .entity(messageModel.getMessage())
                .build();
    }
}

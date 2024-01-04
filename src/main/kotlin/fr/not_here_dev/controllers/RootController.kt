package fr.not_here_dev.controllers

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response


@Path("/")
class RootController{
    @GET
    fun get() = Response
            .status(Response.Status.MOVED_PERMANENTLY)
            .header("Location", "/home")
            .build()
}

package fr.not_here_dev.controllers

import fr.not_here_dev.JTERendering
import fr.not_here_dev.Rendering
import jakarta.ws.rs.Produces
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType


@Path("/home")
@Produces(MediaType.TEXT_HTML)
class HomeResource(val render: JTERendering) {

//    @GET
//    fun get() = "Chansey"
    @GET
    fun get() = render
        .view("home")
        .with("title" to "Home")
}
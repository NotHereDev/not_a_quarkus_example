package fr.not_here_dev.controllers

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/home")
class HomeResource {
    @GET
    fun get() = "Wesh alors"
}
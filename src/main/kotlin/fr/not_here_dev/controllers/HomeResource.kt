package fr.not_here_dev.controllers

import io.quarkus.qute.CheckedTemplate
import io.quarkus.qute.TemplateInstance
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path


@Path("/home")
class HomeResource {
    @CheckedTemplate
    object Templates {
        @JvmStatic
        external fun home(title: String): TemplateInstance
    }

    @GET
    fun get() = Templates.home("Hello page")
}
package fr.not_here_dev.controllers

import fr.not_here_dev.entities.User
import jakarta.validation.Valid
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path

@Path("/users")
class UsersController {

    @GET
    fun index() = User.listAll()

    @Path("{id}")
    @GET
    fun show(id: Long) = User.findById(id)

    @POST
    fun create(@Valid newUser: User): User {
        newUser.persistAndFlush()
        return newUser
    }

}
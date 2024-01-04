package fr.not_here_dev.controllers

import fr.not_here_dev.entities.User
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/users")
class UsersController {

    @GET
    fun index(): List<User> {
        return User.listAll()
    }
}
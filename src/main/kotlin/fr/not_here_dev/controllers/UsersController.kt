package fr.not_here_dev.controllers

import fr.not_here_dev.entities.User
import jakarta.validation.Valid
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response

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

    @Path("/{id}")
    @PATCH
    fun update(id: Long, @Valid user: User): User {
        val u = User.findById(id)!!
        user.id = u.id
        user.persistAndFlush()
        return user
    }

    @Path("/{id}")
    @DELETE
    fun destroy(id: Long): Response {
        User.deleteById(id)
        return Response.ok().build()
    }

}
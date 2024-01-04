package fr.not_here_dev.controllers

import fr.not_here_dev.entities.User
import jakarta.transaction.Transactional
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
    @Transactional
    fun create(@Valid newUser: User): User {
        newUser.persistAndFlush()
        return newUser
    }

    @Path("/{id}")
    @PATCH
    @Transactional
    fun update(id: Long, @Valid newUser: User): User {
        val user = User.findById(id)!!
        user.login = newUser.login
        user.persistAndFlush()
        return user
    }

    @Path("/{id}")
    @DELETE
    @Transactional
    fun destroy(id: Long): Response {
        User.deleteById(id)
        return Response.ok().build()
    }
}

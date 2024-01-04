package fr.not_here_dev.controllers

import fr.not_here_dev.entities.User
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response

@Path("/users")
class UserResource {
    @GET
    fun index() = User.listAll()

    @Path("{id}")
    @GET
    fun show(id: Long) {
        val user = User.findById(id)
        if (user == null) {
            Response.status(Response.Status.NOT_FOUND).build()
        } else {
            Response.ok().entity(user).build()
        }
    }

    @POST
    @Transactional
    fun create(@Valid newUser: User): User {
        newUser.persistAndFlush()
        return newUser
    }

    @Path("/{id}")
    @PATCH
    fun update(id: Long, newUser: User): Response {
        val user = User.findById(id)!!
        user.login = newUser.login
        if(!user.valid){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(user.violationMap)
                    .build()
        }
        user.persist()
        return Response.ok().entity(user).build()
    }

    @Path("/{id}")
    @DELETE
    @Transactional
    fun destroy(id: Long): Response {
        User.deleteById(id)
        return Response.ok().build()
    }
}

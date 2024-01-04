package fr.not_here_dev.exception_mappers

import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.core.Response.*
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

class ConstraintViolationMapper {
    @ServerExceptionMapper
    fun mapConstraintViolation(exception: ConstraintViolationException) = status(Status.BAD_REQUEST)
            .entity(exception.constraintViolations.map { it.propertyPath.toString() to it.message }.toMap())
            .build()

}
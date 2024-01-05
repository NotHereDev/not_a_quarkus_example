package fr.not_here_dev.providers

import io.quarkus.logging.Log
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider


@Provider
internal class LoggingFilter : ContainerRequestFilter, ContainerResponseFilter {
    /* Useful stuff for later development purposes.
       @Context
       UriInfo info;

       @Context
       HttpServerRequest request;
       */
    override fun filter(requestContext: ContainerRequestContext) {
        requestContext.setProperty("requestStartTime", System.nanoTime())
        Log.info("Got [${requestContext.method}] Request to '${requestContext.uriInfo.absolutePath}'")
    }

    override fun filter(
        requestContext: ContainerRequestContext,
        responseContext: ContainerResponseContext
    ) {
        val requestStartTime = requestContext.getProperty("requestStartTime") as Long?
        val requestDur = if (requestStartTime != null) System.nanoTime() - requestStartTime else null
        Log.info("Rendered [${responseContext.status}] response in ${if (requestDur != null) (requestDur / 10_000).toFloat() / 100 else "?"}ms to '${requestContext.uriInfo.absolutePath}', result: ${responseContext.entity}")
    }
}
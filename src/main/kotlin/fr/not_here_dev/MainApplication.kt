package fr.not_here_dev

import jakarta.ws.rs.core.Application
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.info.Info

@OpenAPIDefinition(info = Info(title = "Main API", version = "1.0"))
class MainApplication: Application()
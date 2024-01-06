
import io.quarkus.runtime.StartupEvent
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import jakarta.enterprise.event.Observes

@Suppress("unused") // will be triggered by quarkus
class StaticResources {
    fun installRoute(@Observes startupEvent: StartupEvent?, router: Router) {
        router.route()
            .path("/static/*")
            .handler(StaticHandler.create("static/"))
    }
}
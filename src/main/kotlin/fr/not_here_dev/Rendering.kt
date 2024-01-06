package fr.not_here_dev

import fr.not_here_dev.helpers.SSR
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.ws.rs.WebApplicationException
import lombok.RequiredArgsConstructor
import lombok.extern.java.Log
import org.jboss.resteasy.plugins.providers.html.Renderable
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.context.IExpressionContext
import org.thymeleaf.context.IWebContext
import org.thymeleaf.linkbuilder.StandardLinkBuilder
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets


@Log
@ApplicationScoped
class Rendering {
    @Produces
    fun templateEngine(): TemplateEngine {
        val templateEngine = TemplateEngine()
        templateEngine.setTemplateResolver(ClassLoaderTemplateResolver())
        templateEngine.setLinkBuilder(CustomLinkBuilder())
        return templateEngine
    }

    fun view(relativePath: String?): ThymeleafView {
        val templatePath = String.format("templates/%s.html", relativePath)
        return ThymeleafView(templatePath, templateEngine())
    }

    class CustomLinkBuilder : StandardLinkBuilder() {
        override fun computeContextPath(
            context: IExpressionContext, base: String?, parameters: Map<String, Any>?
        ): String {
            if (context is IWebContext) {
                return super.computeContextPath(context, base, parameters)
            }

            return "./app/src/main/resources/public" //assuming css and images are here
        }
    }

    @RequiredArgsConstructor
    class ThymeleafView(private val path: String, private val templateEngine: TemplateEngine) : Renderable {
        private val variables: MutableMap<String, Any> = mutableMapOf(
            "ssr" to SSR.instance
        )

        fun with(vararg variables: Pair<String, Any>): ThymeleafView {
            this.variables.putAll(variables)
            return this
        }

        @Throws(IOException::class, ServletException::class, WebApplicationException::class)
        override fun render(request: HttpServletRequest, response: HttpServletResponse) {
            val context = Context(request.locale, variables)

            response.outputStream.use { outputStream ->
                OutputStreamWriter(outputStream, StandardCharsets.UTF_8).use { writer ->
                    templateEngine.process(path, context, writer)
                }
            }
        }
    }
}
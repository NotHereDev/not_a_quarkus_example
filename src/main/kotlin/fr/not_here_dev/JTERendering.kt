package fr.not_here_dev

import fr.not_here_dev.helpers.SSR
import fr.not_here_dev.providers.LoggingFilter.Companion.NO_LOGS_REGEX
import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.PrintWriterOutput
import gg.jte.output.StringOutput
import gg.jte.resolve.DirectoryCodeResolver
import io.quarkus.runtime.LaunchMode
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.ws.rs.WebApplicationException
import lombok.RequiredArgsConstructor
import lombok.extern.java.Log
import org.jboss.resteasy.plugins.providers.html.Renderable
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Path


@Log
@ApplicationScoped
class JTERendering {
    @Produces
    fun templateEngine(): TemplateEngine {
        val te = TemplateEngine.create(DirectoryCodeResolver(Path.of("src/main/resources/templates")), ContentType.Html)
        return te
    }

    fun view(relativePath: String?): JTEView {
        val templatePath = String.format("%s.jte", relativePath)
        return JTEView(templatePath, templateEngine())
    }

//    class CustomLinkBuilder : StandardLinkBuilder() {
//        override fun computeContextPath(
//            context: IExpressionContext, base: String?, parameters: Map<String, Any>?
//        ): String {
//            if (context is IWebContext) {
//                return super.computeContextPath(context, base, parameters)
//            }
//
//            return "./app/src/main/resources/public" //assuming css and images are here
//        }
//    }

    @RequiredArgsConstructor
    class JTEView(private val path: String, private val templateEngine: TemplateEngine) : Renderable {
        private val variables: MutableMap<String, Any> = mutableMapOf()

        fun with(vararg variables: Pair<String, Any>): JTEView {
            this.variables.putAll(variables)
            return this
        }

        @Throws(IOException::class, ServletException::class, WebApplicationException::class)
        override fun render(request: HttpServletRequest, response: HttpServletResponse) {
            val logRenderTime = LaunchMode.current() == LaunchMode.DEVELOPMENT &&
                request.queryString?.let { NO_LOGS_REGEX.containsMatchIn(it) } != true

            SSR.instance.logRenderTime = logRenderTime

            //return response.outputStream.writer.write(templateEngine.process(path, context))
            if(logRenderTime) {
                OutputStreamWriter(response.outputStream, StandardCharsets.UTF_8).use {
                    val start = System.nanoTime()
                    val out = StringOutput()
                    templateEngine.render(path, variables, out)
                    it.write(out.toString())
                    val time = System.nanoTime() - start

                    io.quarkus.logging.Log.info("Rendered '${path}' template in ${(time / 10_000).toFloat() / 100}ms")
                }
            } else {
                val out = PrintWriterOutput(response.writer)
                templateEngine.render(path, variables, out)
            }
        }
    }
}
package fr.not_here_dev.helpers

import fr.not_here_dev.utils.threadLocalLazy
import java.io.FileReader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager


@Suppress("unused") // all will be used into views
class SSR {
    companion object{
        val instance: SSR by threadLocalLazy { SSR() }
    }

    var logRenderTime = false

    val SSR_FILE_PATH = "src/main/resources/public/server.js"
    val COMPONENT_WRAPPER="<reactive-component name=\"%s\" props=\"%s\" %s>%s</reactive-component>"

    private val engine
        get() = ScriptEngineManager().getEngineByName("Graal.js")

    val ssrEngine: ScriptEngine by lazy {
        engine.apply {
            eval("function setTimeout(cb, ms) {cb()}")
            eval(FileReader(SSR_FILE_PATH))
        }
    }

    fun propToJson(value: Any): String {
        return when(value){
            is String -> "\"$value\""
            is Int -> "$value"
            is Double -> "$value"
            is Float -> "$value"
            is Long -> "$value"
            is Number -> "$value"
            is Boolean -> if(value) "true" else "false"
            is List<*> -> value.map { propToJson(it!!) }.joinToString(", ", "[", "]")
            is Map<*, *> -> value.map { (key, value) -> "\"$key\": ${propToJson(value!!)}" }.joinToString(", ", "{", "}")
            else -> throw IllegalArgumentException("Unsupported type ${value::class.java}")
        }
    }

    fun reactiveComponent(name: String): String = reactiveComponent(name, mapOf(), true)
    fun reactiveComponent(name: String, ssr: Boolean): String = reactiveComponent(name, mapOf(), ssr)
    fun reactiveComponent(name: String, props: Map<String, Any>): String = reactiveComponent(name, props, true)
    fun reactiveComponent(name: String, props: Map<String, Any> = mapOf(), ssr: Boolean = true): String {
        val jsonProps = propToJson(props)

        val rendered = if(ssr)
            if(logRenderTime)
                ssrEngine.let {
                    val start = System.nanoTime()
                    val r = it.eval("globalThis.renderComponent(\"$name\", $jsonProps)")
                    val time = System.nanoTime() - start

                    io.quarkus.logging.Log.info("Rendered [SSR] of '${name}' component in ${(time / 10_000).toFloat() / 100}ms")
                    r
                }
            else ssrEngine.eval("globalThis.renderComponent(\"$name\", $jsonProps)")
        else ""

        return String.format(COMPONENT_WRAPPER, name, jsonProps, if(ssr) "ssr" else "", rendered)
    }
    fun hydrationScript(): String {
        return if(logRenderTime)
            ssrEngine.let {
                val start = System.nanoTime()
                val r = it.eval("globalThis.generateHydrationScript()")
                val time = System.nanoTime() - start

                io.quarkus.logging.Log.info("Rendered [SSR] of hydration script component in ${(time / 10_000).toFloat() / 100}ms")
                r
            }.toString()
        else ssrEngine.eval("globalThis.generateHydrationScript()").toString()
    }
}
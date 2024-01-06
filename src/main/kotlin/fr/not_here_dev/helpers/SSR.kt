package fr.not_here_dev.helpers

import java.io.FileReader
import javax.script.ScriptEngineManager


@Suppress("unused") // all will be used into views
class SSR {
    companion object{
        val instance: SSR = SSR()
    }

    val SSR_FILE_PATH = "src/main/resources/public/server.js"
    val COMPONENT_WRAPPER="<reactive-component name=\"%s\" props=\"%s\" %s>%s</reactive-component>"

    val engine
        get() = ScriptEngineManager().getEngineByName("Graal.js")

    val ssrEngine
        get() = engine.apply {
            eval("function setTimeout(cb, ms) {cb()}")
            eval(FileReader(SSR_FILE_PATH))
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
        val rendered = if(ssr) ssrEngine.eval("globalThis.renderComponent(\"$name\", $jsonProps)") else ""

        return String.format(COMPONENT_WRAPPER, name, jsonProps, if(ssr) "ssr" else "", rendered)
    }
    fun hydrationScript(): String {
        return ssrEngine.eval("globalThis.generateHydrationScript()").toString()
    }
}
package fr.not_here_dev.helpers

import io.quarkus.logging.Log
import io.quarkus.qute.TemplateGlobal
import javax.script.Compilable
import javax.script.ScriptEngineManager

@Suppress("unused") // all will be used into views
@TemplateGlobal
object SolidJSRenderToString {
    @JvmStatic
    fun reactiveComponent(): String {
        Log.info(ScriptEngineManager().engineFactories)
        val e = ScriptEngineManager().getEngineByName("Graal.js")
        val cmp = (e as Compilable).compile("'hello world'")

        Log.info(cmp.eval())

        return ""
    }
}
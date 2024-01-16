package gg.jte.generated.ondemand;
import fr.not_here_dev.helpers.SSR;
public final class JtehomeGenerated {
	public static final String JTE_NAME = "home.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,8,8,8,8,8,8,8,8,8,8,10,10,10,14,14,14,16,16,16,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String title) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title");
		var __jte_html_attribute_0 = title;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" th:text=\"");
			jteOutput.setContext("title", "th:text");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("title", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">Title</title>\n    <script src=\"static/client.js\"></script>\n    ");
		jteOutput.setContext("head", null);
		jteOutput.writeUserContent(SSR.Companion.getInstance().hydrationScript());
		jteOutput.writeContent("\n</head>\n<body>\ntest\n");
		jteOutput.setContext("body", null);
		jteOutput.writeUserContent(SSR.Companion.getInstance().reactiveComponent("HelloWorld"));
		jteOutput.writeContent("\n</body>\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String title = (String)params.get("title");
		render(jteOutput, jteHtmlInterceptor, title);
	}
}

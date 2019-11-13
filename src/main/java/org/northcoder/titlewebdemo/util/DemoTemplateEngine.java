package org.northcoder.titlewebdemo.util;

import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * The Thymeleaf template engine can be configured here. The engine takes a
 * Thymeleaf template (which typically represents an HTML page), plus a Java
 * key/value map. The engine then loads data from the map, and generates a
 * displayable HTML page from this data.
 *
 * @see <a href="https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf" target="_blank">Thymeleaf Tutorial</a>.
 */
public class DemoTemplateEngine {

    public static void configure() {
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE);
        JavalinThymeleaf.configure(configureEngine());
    }

    private static TemplateEngine configureEngine() {
        // Use the Thymeleaf classloader resolver:
        ClassLoaderTemplateResolver templateResolver
                = new ClassLoaderTemplateResolver(Thread
                        .currentThread().getContextClassLoader());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/thymeleaf/");
        templateResolver.setCacheTTLMs(3600000L); // one hour
        templateResolver.setCacheable(true);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}

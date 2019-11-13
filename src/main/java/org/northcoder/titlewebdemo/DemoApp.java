package org.northcoder.titlewebdemo;

import org.northcoder.titlewebdemo.util.DemoAccessManager;
import org.northcoder.titlewebdemo.util.DemoJetty;
import org.northcoder.titlewebdemo.util.DemoSessionHandler;
import org.northcoder.titlewebdemo.util.DemoTemplateEngine;
import org.northcoder.titlewebdemo.util.LoggerUtils;
import org.northcoder.titlewebdemo.controller.TitleController;
import org.northcoder.titlewebdemo.controller.TalentController;
import static org.northcoder.titlewebdemo.Path.*; // e.g. _Title...
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.Handler;
import io.javalin.core.JavalinConfig;
import static io.javalin.apibuilder.ApiBuilder.*; // before, get, post...

/**
 * Uses <a href="https://javalin.io/documentation">Javalin</a> as the web server.
 * Javalin, in turn, uses embedded Jetty as its default http server.
 */
public class DemoApp {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            configureJavalin(config);
        }).start();

        app.routes(() -> {
            before(TRACK_SESSION);
            get(TITLES, TitleController.fetchAll);
            get(TALENTS, TalentController.fetchAll);

            get(_Title.ROUTE, TitleController.fetchOne);
            post(_Title.ROUTE, TitleController.updateOne);

            get(_Talent.ROUTE, TalentController.fetchOne);
            post(_Talent.ROUTE, TalentController.updateOne);
        });
    }

    private static final Handler TRACK_SESSION = (ctx) -> {
        // Activate jetty session tracking.
        ctx.req.getSession();
    };

    private static void configureJavalin(JavalinConfig config) {
        DemoTemplateEngine.configure();
        config.requestLogger((ctx, millis) -> {
            LoggerUtils.logHttpRequest(ctx.req, ctx.res, millis);
        });
        config.accessManager((handler, ctx, permittedRoles) -> {
            DemoAccessManager.checkAccess(handler, ctx, permittedRoles);
        });
        config.sessionHandler(DemoSessionHandler::create);
        config.registerPlugin(new RouteOverviewPlugin(ROUTES_OVERVIEW));
        config.addStaticFiles(STATIC_FILES);
        config.server(DemoJetty::create);
    }

}

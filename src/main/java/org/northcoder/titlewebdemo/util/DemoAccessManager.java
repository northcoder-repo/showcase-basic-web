package org.northcoder.titlewebdemo.util;

import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import java.util.Set;
import java.util.HashSet;

/**
 * Not used in this demo, but this is where access controls would be implemented,
 * e.g. when a user logs on (read-only user; data editor; administrator; etc.).
 */
public class DemoAccessManager {

    enum DemoRole implements Role {
        READONLY,
        ROLE_ONE,
        ROLE_TWO,
        ROLE_THREE;
    }

    public static void checkAccess(Handler handler, Context ctx,
            Set<Role> permittedRoles) throws Exception {
        // retrieve user stored during login:
        String currentUser = ctx.sessionAttribute("current-user");
        if (currentUser == null) {
            //redirectToLogin(ctx);
            handler.handle(ctx);
        } else if (userHasValidRole(ctx, permittedRoles)) {
            handler.handle(ctx);
        } else {
            throw new UnauthorizedResponse();
        }
    }

    private static boolean userHasValidRole(Context ctx, Set<Role> permittedRoles) {
        Set<Role> userRoles = getUserRoles(ctx);
        boolean hasValidRole = userRoles.stream().anyMatch(permittedRoles::contains);
        return false; // your code here
    }

    private static Set<Role> getUserRoles(Context ctx) {
        // determine user role based on request
        // typically done by inspecting headers
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(DemoRole.READONLY);
        return userRoles;
    }

    private static void redirectToLogin(Context ctx) {
    }
}

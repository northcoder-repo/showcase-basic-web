package org.northcoder.titlewebdemo.dao;

import java.time.temporal.ChronoUnit;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.northcoder.titlewebdemo.util.LoggerUtils;

/**
 * Logs SQL statements, including their bind parameters and execution times.
 */
public class DemoSqlLogger implements SqlLogger {

    @Override
    public void logAfterExecution(StatementContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("SQL statement: [").append(ctx.getRenderedSql())
                .append("] Params: ");
        ctx.getParsedSql().getParameters().getParameterNames().forEach((name) -> {
            sb.append(name).append(": [")
                    .append(ctx.getBinding().findForName(name, ctx).orElse(null))
                    .append("], ");
        });
        if (sb.toString().endsWith("Params: ")) {
            sb.append("none.  ");
        }

        LoggerUtils.LOGGER.info(sb.toString().substring(0, sb.length()-2));
        LoggerUtils.LOGGER.info(String.format("SQL exec time: %s ms", 
                ((double) ctx.getElapsedTime(ChronoUnit.MICROS) / 1000)));
    }

}

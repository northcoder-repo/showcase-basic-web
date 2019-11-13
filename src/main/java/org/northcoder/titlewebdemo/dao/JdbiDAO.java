package org.northcoder.titlewebdemo.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.jdbi.v3.core.Jdbi;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.northcoder.titlewebdemo.beans.DemoBean;
import org.northcoder.titlewebdemo.controller.DaoData;
import org.northcoder.titlewebdemo.util.LoggerUtils;
import org.northcoder.titlewebdemo.util.Utils;

/**
 * The data access object, in which all data access is via the JDBI library.
 * Data is accessed via prepared statements, by providing a SQL statement
 * together with a bean containing any required bind parameters. For select
 * statements, results are captured as one or more beans, loaded from the SQL's
 * result set.<p>
 *
 * Most of the objects involved in this process are saved and passed around in
 * the {@link org.northcoder.titlewebdemo.controller.DaoData DaoData} object,
 * which is used to handle SQL request and response data.
 */
public class JdbiDAO {

    private final Jdbi jdbi;

    public static final String DATABASE_ERROR = "A database error has occurred.";
    public static final String RECORD_NOT_FOUND = "Record was not found.";
    public static final String RECORD_NOT_UPDATED = "Record was not updated.";

    public JdbiDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    /**
     * Selects one or more records from the database - e.g. for a listing of
     * titles, or to get all actors in a specific title, etc.
     *
     * @param <T> the type of bean to use when mapping SQL results to beans.
     * @param daoData an object containing the SQL to execute and the bind
     * parameters to use.
     * @return the DaoData object containing the results of the SQL statement.
     */
    public <T extends DemoBean> DaoData<T> selectRecords(final DaoData<T> daoData) {
        List<T> beans = new ArrayList();
        try {
            beans = jdbi.withHandle(handle -> handle
                    .select(daoData.getSql())
                    .bindBean(daoData.getBindParamsBean())
                    .mapToBean(daoData.getClazz())
                    .list());
        } catch (Exception ex) {
            logException(ex);
            daoData.setDbErrorMessage(DATABASE_ERROR);
        }
        daoData.getResultBeans().addAll(Utils.sortList(beans));
        return daoData;
    }

    /**
     * Selects one record from the database - e.g. when selecting a specific
     * record using its primary key.
     *
     * @param <T> the type of bean to use when mapping SQL result to a bean.
     * @param daoData an object containing the SQL to execute and the bind
     * parameters to use.
     * @return the DaoData object containing the results of the SQL statement.
     */
    public <T extends DemoBean> DaoData<T> selectRecord(final DaoData<T> daoData) {
        try {
            Optional<T> opt = jdbi.withHandle(handle -> handle
                    .select(daoData.getSql())
                    .bindBean(daoData.getBindParamsBean())
                    .mapToBean(daoData.getClazz())
                    .findOne());
            if (opt.isEmpty()) {
                daoData.setResultBean(daoData.getBindParamsBean());
                daoData.setDbErrorMessage(RECORD_NOT_FOUND);
                LoggerUtils.LOGGER.warn(RECORD_NOT_FOUND);
            } else {
                daoData.setResultBean(opt.get());
            }
        } catch (Exception ex) {
            logException(ex);
            daoData.setDbErrorMessage(DATABASE_ERROR);
            daoData.setResultBean(daoData.getBindParamsBean());
        }
        return daoData;
    }

    /**
     * Updates one record in the database.
     * @param <T> the type of bean representing the record to be updated.
     * @param daoData an object containing the SQL to execute and the bind
     * parameters to use.
     * @return the DaoData object containing the results of the SQL update.
     */
    public <T extends DemoBean> DaoData<T> updateRecord(final DaoData<T> daoData) {
        Integer rowsAffected = null;
        try {
            rowsAffected = jdbi.withHandle(handle -> handle
                    .createUpdate(daoData.getSql())
                    .bindBean(daoData.getBindParamsBean())
                    .execute());
        } catch (Exception ex) {
            logException(ex);
            daoData.setDbErrorMessage(getErrorMessage(ex));
        }
        daoData.setAffectedRowCount(rowsAffected);
        if (rowsAffected != null && rowsAffected == 0) {
            daoData.setDbErrorMessage(RECORD_NOT_UPDATED);
            LoggerUtils.LOGGER.warn(RECORD_NOT_UPDATED);
        }
        return daoData;
    }

    public static void logException(Exception ex) {
        LoggerUtils.LOGGER.error(getErrorMessage(ex), ex);
    }

    /**
     * 
     * @param ex the database exception
     * @return a summary of the error message in the database exception
     */
    public static String getErrorMessage(Exception ex) {
        // just the message part - discard the exception class name...
        // "{ClassNameWithoutPackage}: {ThrowableMessage. Second sentence...}"
        // And if the message is long, just get the first sentence.
        String[] errorMessageParts = ExceptionUtils.getRootCauseMessage(ex).split(":", 2);
        int index = (errorMessageParts.length >= 2) ? 1 : 0;
        return errorMessageParts[index].split("\\.", 2)[0].trim();
    }

}

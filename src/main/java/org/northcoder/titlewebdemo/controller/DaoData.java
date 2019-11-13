package org.northcoder.titlewebdemo.controller;

import org.northcoder.titlewebdemo.beans.DemoBean;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.Validate;

/**
 * Used to handle SQL request and response data. See also
 * {@link org.northcoder.titlewebdemo.dao.JdbiDAO JdbiDAO}.
 *
 * @param <T> The type of bean into which results will be loaded. Also the same
 * type is used by the bind parameters object.
 */
public class DaoData<T extends DemoBean> {

    /**
     * @param bindParamsBean A bean containing the sql bind parameters, to be
     * passed into the DAO method when executing a SQL statement. The values in
     * the bean are bound to named parameters in the specific SQL statement
     * being executed.<p>
     *
     * SQL named parameters must have names which match the bean's field names.
     * For example, a database column called "building_name" would be bound by
     * using the bean field "buildingName"
     * .<p>
     *
     * When retrieving data, this bean will contain the selection parameters -
     * for example, just one value for the primary key ID, when selecting a
     * single record. The data in such beans is not pre-validated.<p>
     *
     * When updating a record, the bean must contain all required field values
     * (nulls in the bean will result in nulls in the database record). The data
     * in such beans should be pre-validated before being passed to the
     * database.<p>
     *
     * If the SQL statement does not contain any bind parameters, an empty bean
     * instance must be provided.
     * @param sql the SQL statement to be executed.
     */
    public DaoData(T bindParamsBean, String sql) {
        Validate.notNull(bindParamsBean, "The bind parameters bean cannot be null.");
        Validate.notBlank(sql, "The SQL string cannot be null, empty, or blank.");
        this.bindParamsBean = bindParamsBean;
        this.sql = sql;
        this.clazz = bindParamsBean.getClass();
    }

    private final T bindParamsBean;
    private final Class clazz;
    private final String sql;
    private T resultBean;
    private List<T> resultBeans = new ArrayList();
    private Integer affectedRowCount;
    private Integer assignedSequenceID;
    private String dbErrorMessage;

    public T getBindParamsBean() {
        return bindParamsBean;
    }

    /**
     * @return The class of the bean used for bind parameters and results. Used
     * by Jdbi when converting a result set to beans.
     */
    public Class getClazz() {
        return clazz;
    }

    /**
     * @return The SQL to be executed - it must always use bind parameters for
     * all user-provided values: all SQL uses prepared statements.
     */
    public String getSql() {
        return sql;
    }

    /**
     * @return Used when the SQL statement is expected to retrieve no more than
     * one row.
     */
    public T getResultBean() {
        return resultBean;
    }

    public void setResultBean(T resultBean) {
        this.resultBean = resultBean;
    }

    /**
     * @return Used when the SQL statement may retrieve multiple rows.
     */
    public List<T> getResultBeans() {
        return resultBeans;
    }

    public void setResultBeans(List<T> resultBeans) {
        this.resultBeans = resultBeans;
    }

    /**
     * @return The number of rows affected by an insert, update, or delete. Not
     * used for selects.
     */
    public Integer getAffectedRowCount() {
        return affectedRowCount;
    }

    public void setAffectedRowCount(Integer affectedRowCount) {
        this.affectedRowCount = affectedRowCount;
    }

    /**
     * @return The auto-assigned primary key value of a newly created row, as
     * generated in the database. Depends on the specific DB table's
     * implementation.
     */
    public Integer getAssignedSequenceID() {
        return assignedSequenceID;
    }

    public void setAssignedSequenceID(Integer assignedSequenceID) {
        this.assignedSequenceID = assignedSequenceID;
    }

    /**
     * @return A summary of the database error message, if an exception was
     * thrown.
     */
    public String getDbErrorMessage() {
        return dbErrorMessage;
    }

    public void setDbErrorMessage(String dbErrorMessage) {
        this.dbErrorMessage = dbErrorMessage;
    }

}

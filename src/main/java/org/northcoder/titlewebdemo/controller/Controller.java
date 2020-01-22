package org.northcoder.titlewebdemo.controller;

import org.northcoder.titlewebdemo.beans.DemoBean;
import org.northcoder.titlewebdemo.dao.JdbiDAO;
import org.northcoder.titlewebdemo.util.Utils;

/**
 * The bridge between specific bean controllers and the generic data access object.
 */
public abstract class Controller {

    private final JdbiDAO jdbiDAO;
    
    public Controller(JdbiDAO jdbiDAO) {
        this.jdbiDAO = jdbiDAO;
    }

    /**
     * Selects one record from the database, using the record's unique identifier
     * (primary key).
     *
     * @param <T> the type of bean being selected
     * @param bindParams a bean containing all null fields except for the unique
     * identifier field
     * @param sql the select statement to be executed
     * @return the result set (expected to be a single bean)
     */
    public <T extends DemoBean> DaoData<T> fetchOneRecord(T bindParams, String sql) {
        DaoData<T> daoData = new DaoData(bindParams, sql);
        return jdbiDAO.selectRecord(daoData);
    }

    /**
     * Selects a set of records from the database.
     *
     * @param <T> the type of beans being selected
     * @param bindParams typically, an empty bean with no database bind
     * parameters, if all data is being selected.
     * @param sql the select statement
     * @return a collection of beans representing the SQL result set
     */
    public <T extends DemoBean> DaoData<T> fetchAllRecords(T bindParams, String sql) {
        DaoData<T> daoData = new DaoData(bindParams, sql);
        return jdbiDAO.selectRecords(daoData);
    }

    /**
     * Updates one record in the database.
     *
     * @param <T> the type of bean being updated
     * @param formBody the raw contents of the HTML form, as submitted by the
     * user
     * @param updateSql the SQL update statement for the type of bean being
     * updated
     * @param bean an empty bean into which the raw form data will be loaded
     * @return the updated record, upon successful completion of the SQL update;
     * or, the original form data plus error messages, if there were any form
     * validation errors or database errors.
     */
    public <T extends DemoBean> DaoData<T> updateOneRecord(String formBody,
            String updateSql, T bean) {

        // load data from the html form into the bean:
        T bindParams = Utils.formBodyToBean(formBody, bean);
        DaoData<T> daoData = new DaoData(bindParams, updateSql);
        daoData.setResultBean(bindParams);

        // check for form datatype parse errors (e.g. text in a number field):
        if (!bindParams.getFormValidationErrors().isEmpty()) {
            // send the form data back to the user for corrections:
            daoData.getResultBean().setActionCompletedOK(false);
            return daoData;
        }

        // check for form validation errors (e.g. hibernate validators):
        bindParams.validateFormData();
        if (!bindParams.getFormValidationErrors().isEmpty()) {
            // send the form data back to the user for corrections:
            daoData.setResultBean(bindParams);
            daoData.getResultBean().setActionCompletedOK(false);
            return daoData;
        }

        // there were no errors in the form - so now we can try to update the DB:
        daoData = jdbiDAO.updateRecord(daoData);
        if (daoData.getDbErrorMessage() != null) {
            // there was a DB constraint violation or other DB error:
            daoData.setResultBean(bindParams);
            daoData.getResultBean().setActionCompletedOK(false);
            return daoData;
        }

        // if we got this far, the db update succeeded:
        daoData.getResultBean().setActionCompletedOK(true);
        return daoData;
    }

}

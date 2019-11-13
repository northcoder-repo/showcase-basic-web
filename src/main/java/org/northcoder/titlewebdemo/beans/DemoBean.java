package org.northcoder.titlewebdemo.beans;

import java.util.List;
import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import org.northcoder.titlewebdemo.validation.ValidationHandler;
import org.northcoder.titlewebdemo.validation.ValidationError;

/**
 * All Demo JavaBeans inherit from this class.
 */
public abstract class DemoBean {

    /**
     * Indicates the result of a database insert/update/delete - whether the
     * action completed successfully, or if there were errors.
     *
     * Database selects do not have action outcomes - the boolean is set to
     * null.
     */
    private transient Boolean actionCompletedOK = null;

    private transient List<ValidationError> formValidationErrors = new ArrayList();
        
    private transient String sql;
    
    public List<ValidationError> getFormValidationErrors() {
        return formValidationErrors;
    }

    public void validateFormData() {
        formValidationErrors = ValidationHandler.getValidationErrors(this);
    }

    public String getBeanDataAsJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public String getFormValidationErrorsAsJson() {
        return new GsonBuilder().create().toJson(formValidationErrors);
    }

    public Boolean getActionCompletedOK() {
        return actionCompletedOK;
    }

    public void setActionCompletedOK(Boolean actionCompletedOK) {
        this.actionCompletedOK = actionCompletedOK;
    }

}

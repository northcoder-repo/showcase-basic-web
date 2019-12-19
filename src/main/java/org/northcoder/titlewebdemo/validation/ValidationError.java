package org.northcoder.titlewebdemo.validation;

import java.util.Locale;

/**
 * A convenience class for handling validation errors.
 */
public class ValidationError {
    
    // this captures parse errors (e.g. text in a number field):
    public ValidationError(String field, String value, String dataType) {
        this.field = field;
        this.value = value;
        this.dataType = dataType;
        this.message = buildMessage();
    }
    
    // this one captures Hibernate validation errors:
    public ValidationError(String field, String message) {
        this.field = field;
        this.value = null;
        this.dataType = null;
        this.message = message;
    }
    
    private final String field;
    private final String value;
    private final String dataType;
    private final String message;
    
    private String buildMessage() {
        String msg;
        switch(dataType) {
            case "Integer":
            case "Long":
                msg = "Please enter a whole number (no decimal places).";
                break;
            case "Float":
            case "Double":
                msg = "Please enter a number (decimal places allowed).";
                break;
            default:
                msg = String.format("Unable to save this data as a valid %s", 
                        dataType.toLowerCase(Locale.getDefault()));
        }
        return msg;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public String getDataType() {
        return dataType;
    }

    public String getMessage() {
        return message;
    }
    
}

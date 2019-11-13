package org.northcoder.titlewebdemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.northcoder.titlewebdemo.util.StringCleanser;

/**
 * Controls which fields in a data bean cannot be null. Typically used to reflect
 * the nullability of the related database table's columns.
 */
public class MandatoryValidator implements ConstraintValidator<Mandatory, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext cvc) {
        if (obj == null) {
            // handles cases where non-string fields are being evaluated (e.g. Integers):
            return false;
        }
        // some extra checks are needed for String/Char fields, which may not
        // be null - but which are effectively empty or blank, after being
        // cleansed (because the cleansed string is what gets saved to the DB):
        if (obj instanceof String) {
            String s = (String) obj;
            return !(StringCleanser.cleanse(s)).isEmpty();
        } else if (obj instanceof char[]) {
            char[] c = (char[]) obj;
            return !(StringCleanser.cleanse(String.valueOf(c))).isEmpty();
        } else if (obj instanceof java.util.UUID) {
            return true;
        }
        return true;
    }

}

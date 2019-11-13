package org.northcoder.titlewebdemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.northcoder.titlewebdemo.beans.Title;
import org.northcoder.titlewebdemo.beans.Talent;

/**
 * For a pair of fields, verify that both have values, or only the first one has
 * a value, or both are null. Example: for a title with Start Year and End Year,
 * you cannot have a non-null End Year with a null Start Year. The other 3
 * combinations are OK.
 */
public class FirstOrBothValidator implements ConstraintValidator<FirstOrBoth, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext cvc) {
        if (obj instanceof Title) {
            Title title = (Title) obj;
            return !(title.getStartYear() == null && title.getEndYear() != null);
        } else if (obj instanceof Talent) {
            Talent talent = (Talent) obj;
            return !(talent.getBirthYear()== null && talent.getDeathYear()!= null);
        } else {
            return true;
        }
    }

}

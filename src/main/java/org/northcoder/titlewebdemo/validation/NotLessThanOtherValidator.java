package org.northcoder.titlewebdemo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.northcoder.titlewebdemo.beans.Title;
import org.northcoder.titlewebdemo.beans.Talent;

/**
 * For a pair of numeric fields, verify that the second field does not have a
 * value which is less than the first field's value. For example, a Title's
 * Start Year and End Year fields. In this case, the End Year cannot be before
 * the Start Year.
 */
public class NotLessThanOtherValidator implements ConstraintValidator<NotLessThanOther, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext cvc) {
        if (obj instanceof Title) {
            Title title = (Title) obj;
            return !(title.getStartYear() != null && title.getEndYear() != null
                    && title.getEndYear() < title.getStartYear());
        } else if (obj instanceof Talent) {
            Talent talent = (Talent) obj;
            return !(talent.getBirthYear() != null && talent.getDeathYear() != null
                    && talent.getDeathYear() < talent.getBirthYear());
        } else {
            return true;
        }
    }
}

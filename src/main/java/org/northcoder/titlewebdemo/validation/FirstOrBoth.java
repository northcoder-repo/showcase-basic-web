package org.northcoder.titlewebdemo.validation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * For a pair of fields, ensures there cannot be a value in only the second
 * field. Valid combinations are: (1) both fields are populated; (2) neither
 * field is populated; (3) only the first field is populated.
 *
 * Example: A TV series' start year and end year - you cannot provide an end
 * year without also providing a start year.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = FirstOrBothValidator.class)
public @interface FirstOrBoth {

    String message() default "Please provide values in both fields, or only in the first (or leave both blank).";

    String firstField();

    String secondField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

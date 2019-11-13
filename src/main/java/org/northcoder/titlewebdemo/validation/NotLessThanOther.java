package org.northcoder.titlewebdemo.validation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * See
 * {@link org.northcoder.titlewebdemo.validation.NotLessThanOtherValidator NotLessThanOtherValidator}.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = NotLessThanOtherValidator.class)
public @interface NotLessThanOther {

    String message() default "{secondField} cannot be before or less than {firstField}.";

    String firstField();

    String secondField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

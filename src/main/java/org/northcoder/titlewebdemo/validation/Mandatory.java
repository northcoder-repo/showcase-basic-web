package org.northcoder.titlewebdemo.validation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * See
 * {@link org.northcoder.titlewebdemo.validation.MandatoryValidator MandatoryValidator}
 * for details.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MandatoryValidator.class)
public @interface Mandatory {

    String message() default "Please provide a value.";

    String fieldName();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

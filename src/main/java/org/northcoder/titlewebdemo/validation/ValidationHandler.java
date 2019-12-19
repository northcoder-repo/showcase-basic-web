package org.northcoder.titlewebdemo.validation;

import org.northcoder.titlewebdemo.beans.DemoBean;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import com.google.gson.Gson;
import java.util.Locale;

/**
 * Executes <a href="https://hibernate.org/validator/">Hibernate validation
 * checks</a>.
 */
public class ValidationHandler {

    // Located in the default package, with ".properties" suffix:
    private static final String MESSAGE_FILE = "ValidationMessages_en";
    public static final String MANDATORY = "{org.northcoder.titlewebdemo.validation.Mandatory.message}";
    public static final String MIN_MAX_LENGTH = "{org.northcoder.titlewebdemo.validation.MinMaxLength.message}";
    public static final String UUID_MESSAGE = "{org.northcoder.titlewebdemo.validation.Uuid.message}";
    public static final String UUID_REGEX = "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}$";
    public static final String FIRST_OR_BOTH_MESSAGE = "{org.northcoder.titlewebdemo.validation.FirstOrBoth.message}";
    public static final String NOT_LESS_THAN_OTHER_MESSAGE = "{org.northcoder.titlewebdemo.validation.NotLessThanOther.message}";

    public static List<ValidationError> getValidationErrors(DemoBean bean) {
        final Validator validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(
                        new ResourceBundleMessageInterpolator(
                                new PlatformResourceBundleLocator(MESSAGE_FILE)
                        )
                )
                .buildValidatorFactory()
                .getValidator();

        final List<ValidationError> errors = new ArrayList<>();
        final Set<ConstraintViolation<DemoBean>> violations = validator.validate(bean);
        violations.stream().forEach((violation) -> {
            errors.add(new ValidationError(getErrorKey(violation), violation.getMessage()));
        });
        return errors;
    }

    private static String getErrorKey(ConstraintViolation<DemoBean> violation) {
        String key = violation.getPropertyPath().toString();
        if (key != null && !key.isEmpty()) {
            return key;
        } else {
            // Errors that do not tie back to one single field - e.g. "all or none" checks:
            return String.format("class-level-error", violation.getRootBeanClass()
                    .getSimpleName().toLowerCase(Locale.getDefault()));
        }
    }

    public static String errorsToJson(Map<String, String> errors) {
        return new Gson().toJson(errors);
    }

    public static Map<String, String> errorsFromJson(String errors) {
        return new Gson().fromJson(errors, Map.class);
    }

}

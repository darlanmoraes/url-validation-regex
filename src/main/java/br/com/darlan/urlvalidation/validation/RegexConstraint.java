package br.com.darlan.urlvalidation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegexValidator.class)
@Target({
    ElementType.METHOD,
    ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexConstraint {
    String message() default "Invalid regex pattern";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

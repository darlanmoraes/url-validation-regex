package br.com.darlan.urlvalidation.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class RegexValidator implements ConstraintValidator<RegexConstraint, String> {

    @Override
    public void initialize(final RegexConstraint constraint) { }

    @Override
    public boolean isValid(final String regex, final ConstraintValidatorContext context) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (final PatternSyntaxException e) {
            return false;
        }
    }

}
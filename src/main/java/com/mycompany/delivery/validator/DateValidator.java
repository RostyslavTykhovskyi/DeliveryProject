package com.mycompany.delivery.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidDate, LocalDate> {
    @Override
    public void initialize(ValidDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return !date.isBefore(LocalDate.now().plusDays(ValidationConstants.MIN_DAYS_AFTER_ORDER)) &&
                !date.isAfter(LocalDate.now().plusDays(ValidationConstants.MAX_DAYS_AFTER_ORDER));
    }
}

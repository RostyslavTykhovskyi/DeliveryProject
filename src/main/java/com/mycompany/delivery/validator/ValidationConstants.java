package com.mycompany.delivery.validator;

public interface ValidationConstants {
    String USERNAME_PATTERN = "^[a-zA-Z]\\w{4,19}$";
    String EMAIL_PATTERN = "^[\\w]+(?:\\.[\\w]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,6}$";

    String MIN_DIMENSION = "0.01";
    String MAX_DIMENSION = "3.00";
    String MIN_WEIGHT = "0.01";
    String MAX_WEIGHT = "1000.00";
    int MIN_DAYS_AFTER_ORDER = 3;
    int MAX_DAYS_AFTER_ORDER = 30;
}

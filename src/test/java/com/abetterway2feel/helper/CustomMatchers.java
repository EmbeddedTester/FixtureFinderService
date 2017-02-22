package com.abetterway2feel.helper;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public abstract class CustomMatchers {
    public static final Matcher<String> is_not_an_empty_or_null_string = not(isEmptyOrNullString());
}

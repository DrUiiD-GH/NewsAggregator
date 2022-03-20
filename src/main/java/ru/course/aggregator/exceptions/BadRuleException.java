package ru.course.aggregator.exceptions;

public class BadRuleException extends Exception {

    public BadRuleException(String msg) {
        super(msg);
    }

    public BadRuleException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}

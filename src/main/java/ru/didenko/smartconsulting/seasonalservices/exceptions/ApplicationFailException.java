package ru.didenko.smartconsulting.seasonalservices.exceptions;

public class ApplicationFailException extends IllegalArgumentException {
    public ApplicationFailException(String message) {
        super(message);
    }
}

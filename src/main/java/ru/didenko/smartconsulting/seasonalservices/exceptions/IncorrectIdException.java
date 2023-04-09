package ru.didenko.smartconsulting.seasonalservices.exceptions;

import java.util.NoSuchElementException;

public class IncorrectIdException extends NoSuchElementException {

    public IncorrectIdException(String ex) {
        super(ex);
    }
}

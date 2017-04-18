package com.abetterway2feel.fixturefinder.repository.document;

final public class MatchDayNotFoundException extends RuntimeException {

    MatchDayNotFoundException(String message, Throwable cause ) {
        super(message, cause);
    }
}

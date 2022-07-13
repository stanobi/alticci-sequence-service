package pt.winprovit.exercise.exception;

import org.springframework.http.HttpStatus;

public class AlticciException extends Exception {

    private final HttpStatus httpStatus;

    public AlticciException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
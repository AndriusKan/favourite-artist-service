package com.favouriteartistservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = RestController.class)
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { BadInputException.class })
    protected ResponseEntity<ErrorBody> handleResourceNotFoundException(BadInputException ex, WebRequest request) {
        ErrorBody errorBody = new ErrorBody(ex.getMessage());
        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { ItunesIntegrationException.class })
    protected ResponseEntity<ErrorBody> handleItunesIntegrationException(ItunesIntegrationException ex, WebRequest request) {
        ErrorBody errorBody = new ErrorBody(ex.getMessage());
        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static class ErrorBody {
        private String errorMessage;

        public ErrorBody(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

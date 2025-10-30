package com.example.zellervandecastellpatientenverwaltung.presentation.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException.class)
    public ProblemDetail handleCustomNotFound(com.example.zellervandecastellpatientenverwaltung.exceptions.NotFoundException ex, WebRequest request) {
        log.info("Not found: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Nicht gefunden");
        pd.setDetail(ex.getMessage());
        pd.setProperty("exception", ex.getClass().getSimpleName());
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", request.getDescription(false).replace("uri=", ""));
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        log.warn("Bad request: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Ungültige Anfrage");
        pd.setDetail(ex.getMessage());
        pd.setProperty("exception", ex.getClass().getSimpleName());
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("path", request.getDescription(false).replace("uri=", ""));
        return pd;
    }

    @ExceptionHandler(Throwable.class)
    public ProblemDetail handleAllException(Throwable ex, WebRequest request) {
        log.error("Unhandled exception: ", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Unerwarteter Fehler");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setProperty("exception", ex.getClass().getSimpleName());
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("path", request.getDescription(false).replace("uri=", ""));
        return problemDetail;
    }
}

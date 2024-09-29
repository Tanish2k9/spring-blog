package com.tanish.Blog_springboot.exception;

import com.tanish.Blog_springboot.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, WebRequest request){
        List<String> allErrors = new ArrayList<>();
        allErrors.add(ex.getMessage());
        ApiError errorResponse = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(allErrors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentialsExceptionException(InvalidCredentialsException ex, WebRequest request){
        List<String> allErrors = new ArrayList<>();
        allErrors.add(ex.getMessage());
        ApiError errorResponse = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(allErrors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ApiError apiError = new ApiError();
        List<String> allErrors = new ArrayList<>();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getAllErrors().forEach(error -> {
            allErrors.add(error.getDefaultMessage());
        });

        apiError.setErrors(allErrors);
        ex.printStackTrace();

        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationExceptionn(DataIntegrityViolationException ex){
        ApiError apiError = new ApiError();
        List<String> allErrors = new ArrayList<>();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            allErrors.add(error.getDefaultMessage());
//        });
        allErrors.add(ex.getMessage());
        ex.printStackTrace();
        apiError.setErrors(allErrors);
        ex.printStackTrace();

        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllException(Exception ex, WebRequest request){
        List<String> allErrors = new ArrayList<>();
        allErrors.add(ex.getMessage());
        ApiError errorResponse = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(allErrors)
                .build();
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

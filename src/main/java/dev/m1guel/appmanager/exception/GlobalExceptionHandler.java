package dev.m1guel.appmanager.exception;

import dev.m1guel.appmanager.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleGlobalException(Exception e) {
        return new ResponseEntity<>(
                new ResponseDto<>(false, "Internal server error: " + e.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            FieldError firstError = ex.getBindingResult().getFieldErrors().getFirst();
            return new ResponseEntity<>(
                    new ResponseDto<>(false, firstError.getDefaultMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                new ResponseDto<>(false, "Invalid input.", null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        if (!e.getConstraintViolations().isEmpty()) {
            String firstError = e.getConstraintViolations().iterator().next().getMessage();
            return new ResponseEntity<>(
                    new ResponseDto<>(false, firstError, null),
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                new ResponseDto<>(false, "Validation error.", null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleUserNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseDto<>(false, ex.getMessage(), null)
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ResponseDto<String>> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ResponseDto<>(false, ex.getMessage(), null)
        );
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ResponseDto<String>> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseDto<>(false, ex.getMessage(), null)
        );
    }

}
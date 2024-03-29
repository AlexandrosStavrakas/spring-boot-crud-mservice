package gr.crud.service.exceptionHandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GenericExceptionHandler {

    @Autowired
    ObjectMapper objectMapper;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(violation -> violation.getField() + ": " + violation.getDefaultMessage() + "for value " + violation.getRejectedValue())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectMapper.writeValueAsString(ex.getMessage()));
    }

    @ExceptionHandler(UsersEmailAlreadyInUseException.class)
    public ResponseEntity<Object> handleUsersEmailAlreadyInUseException(UsersEmailAlreadyInUseException ex) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.writeValueAsString(ex.getMessage()));
    }

}

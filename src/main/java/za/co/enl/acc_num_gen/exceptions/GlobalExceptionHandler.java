package za.co.enl.acc_num_gen.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import za.co.enl.acc_num_gen.models.APIResponse;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        LOG.error(MessageFormat.format("Error encountered while validating the request {0}", errors));
        return ResponseEntity.badRequest().body(new APIResponse(400, "Bad Request", errors, Instant.now()));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> handleEnumValidationExceptions(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Filed validation error", "Please enter valid data");
        LOG.error(MessageFormat.format("Error encountered while validating the request {0}", ex.getMessage()));
        return ResponseEntity.badRequest().body(new APIResponse(400, "Bad Request", errors, Instant.now()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
            errors.put("Error", ex.getMessage());

        LOG.error(MessageFormat.format("Error encountered while processing the request {0}", errors));
        return ResponseEntity.badRequest().body(new APIResponse(400, "Bad Request", errors, Instant.now()));
    }


}

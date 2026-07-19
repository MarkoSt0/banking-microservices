package rs.ac.bg.fon.accountservice.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.postgresql.util.PSQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex){
        Throwable cause = ex;

        while (cause != null) {
            if (cause instanceof PSQLException psqlException) {

                String message = psqlException
                        .getServerErrorMessage()
                        .getMessage();

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(message);
            }

            cause = cause.getCause();
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Database error occurred.");
    }
}

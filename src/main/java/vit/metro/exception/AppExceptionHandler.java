package vit.metro.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class AppExceptionHandler {

	// method called when user's email violates the pattern
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HashMap<String, String>> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    HashMap<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return new ResponseEntity<HashMap<String, String>>(errors, HttpStatus.NOT_ACCEPTABLE);
	}
	
	// method called when user is not found
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(UserNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }
    
    // method called when saved user already exists in database
    @ExceptionHandler({DuplicateUserException.class})
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException e) {
        return error(HttpStatus.BAD_REQUEST, e);
    }
	
    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        return ResponseEntity.status(status).body(e.getMessage());
    }
}

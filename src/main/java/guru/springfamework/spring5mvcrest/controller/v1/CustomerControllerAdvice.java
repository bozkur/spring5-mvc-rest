package guru.springfamework.spring5mvcrest.controller.v1;

import guru.springfamework.spring5mvcrest.service.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFoundException() {
        return new ResponseEntity("Resource can not be found", HttpStatus.NOT_FOUND);
    }
}

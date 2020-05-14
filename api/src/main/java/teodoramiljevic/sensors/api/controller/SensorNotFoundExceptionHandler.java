package teodoramiljevic.sensors.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import teodoramiljevic.sensors.api.exception.NotFoundException;
import teodoramiljevic.sensors.api.exception.SensorException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class SensorNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={NotFoundException.class})
    protected ResponseEntity<Object> handle(final SensorException ex, final WebRequest request){
        return handleExceptionInternal(ex, ex.getKey(), new HttpHeaders(), NOT_FOUND, request);
    }
}

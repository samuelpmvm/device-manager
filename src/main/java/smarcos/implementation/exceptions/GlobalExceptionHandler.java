package smarcos.implementation.exceptions;

import com.model.device.ApiErrorCode;
import com.model.device.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.error("Http message not readable exception found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ApiErrorCode.BAD_REQUEST, "Invalid request body format", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.error("Method argument not valid exception found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ApiErrorCode.BAD_REQUEST, "Missing required attributes. Name, brand and state are mandatory.", ex.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        LOGGER.error("Http media type not supported exception:{}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiErrorResponse(ApiErrorCode.MEDIA_TYPE_NOT_SUPPORTED, "Unsupported Media Type. For more details check the documentation", exception.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        LOGGER.error("Http request method not supported exception:{}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ApiErrorResponse(ApiErrorCode.METHOD_NOT_ALLOWED, "Method not allowed", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception exception) {
        LOGGER.error("Exception found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(ApiErrorCode.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred. For more information contact the application responsible",
                        exception.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        LOGGER.error("No resource found exception found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(ApiErrorCode.RESOURCE_NOT_FOUND,
                        "Resource not found. For more details check the documentation",
                        exception.getMessage()));
    }
}

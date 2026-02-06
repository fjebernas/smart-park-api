package dev.fjcodes.config;

import dev.fjcodes.exception.IllegalCheckInOutException;
import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleBaseException(Exception ex, WebRequest request) {
		log.error(ex.getMessage());

		final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		ErrorResponse errorResponse = new ErrorResponse()
				.setStatus(status.value())
				.setSummary("Error Occurred")
				.setErrors(Collections.singletonList("Something went wrong. Please contact the administrator."))
				.setPath(path)
				.setTimestamp(LocalDateTime.now().toString())
				.setMethod(Objects.requireNonNull(((ServletWebRequest) request).getHttpMethod()).toString());
		return handleExceptionInternal(
				ex,
				errorResponse,
				new HttpHeaders(),
				status,
				request
		);
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		final HttpStatus status = HttpStatus.BAD_REQUEST;
		final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		List<String> errors = Collections.singletonList(ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse()
				.setStatus(status.value())
				.setSummary("Requested Resource Not Found")
				.setErrors(errors)
				.setPath(path)
				.setTimestamp(LocalDateTime.now().toString())
				.setMethod(Objects.requireNonNull(((ServletWebRequest) request).getHttpMethod()).toString());
		return handleExceptionInternal(
				ex,
				errorResponse,
				new HttpHeaders(),
				status,
				request
		);
	}

	@ExceptionHandler(value = IllegalCheckInOutException.class)
	protected ResponseEntity<Object> handleIllegalCheckInOutException(IllegalCheckInOutException ex, WebRequest request) {
		final HttpStatus status = HttpStatus.BAD_REQUEST;
		final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		List<String> errors = Collections.singletonList(ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse()
				.setStatus(status.value())
				.setSummary("Invalid Operation")
				.setErrors(errors)
				.setPath(path)
				.setTimestamp(LocalDateTime.now().toString())
				.setMethod(Objects.requireNonNull(((ServletWebRequest) request).getHttpMethod()).toString());
		return handleExceptionInternal(
				ex,
				errorResponse,
				new HttpHeaders(),
				status,
				request
		);
	}

	@ExceptionHandler(value = ResourceAlreadyExistsException.class)
	protected ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
		final HttpStatus status = HttpStatus.BAD_REQUEST;
		final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		List<String> errors = Collections.singletonList(ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse()
				.setStatus(status.value())
				.setSummary("Resource Already Exists")
				.setErrors(errors)
				.setPath(path)
				.setTimestamp(LocalDateTime.now().toString())
				.setMethod(Objects.requireNonNull(((ServletWebRequest) request).getHttpMethod()).toString());
		return handleExceptionInternal(
				ex,
				errorResponse,
				new HttpHeaders(),
				status,
				request
		);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		final HttpStatus status = HttpStatus.BAD_REQUEST;
		final String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		List<String> errors = Collections.singletonList(ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse()
				.setStatus(status.value())
				.setSummary("Illegal Arguments on Payload")
				.setErrors(errors)
				.setPath(path)
				.setTimestamp(LocalDateTime.now().toString())
				.setMethod(Objects.requireNonNull(((ServletWebRequest) request).getHttpMethod()).toString());
		return handleExceptionInternal(
				ex,
				errorResponse,
				new HttpHeaders(),
				status,
				request
		);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final String path = ((ServletWebRequest) request).getRequest().getRequestURI();

		List<String> errors = ex.getAllErrors().stream().map(e -> ((FieldError) e).getField() + " " + e.getDefaultMessage()).collect(Collectors.toList());

		ErrorResponse errorResponse = new ErrorResponse()
				.setStatus(status.value())
				.setSummary("Invalid values found in payload")
				.setErrors(errors)
				.setPath(path)
				.setTimestamp(LocalDateTime.now().toString())
				.setMethod(Objects.requireNonNull(((ServletWebRequest) request).getHttpMethod()).toString());

		return handleExceptionInternal(
				ex,
				errorResponse,
				new HttpHeaders(),
				status,
				request
		);
	}
}

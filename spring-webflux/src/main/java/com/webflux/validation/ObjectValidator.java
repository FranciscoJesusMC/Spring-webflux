package com.webflux.validation;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.webflux.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class ObjectValidator {

	private final Validator validator;
	
	@SneakyThrows
	public <T> T validate(T object) {
		Set<ConstraintViolation<T>> errors = validator.validate(object);
		if(errors.isEmpty()) {
			return object;
		}else {
			String message = errors.stream().map(err -> err.getMessage()).collect(Collectors.joining());
			throw new CustomException(HttpStatus.BAD_REQUEST, message);
		}
	}
	
}

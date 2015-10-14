package net.djb.budget.rest.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;

import net.djb.budget.rest.exception.UnbalancedTransactionException;

@ControllerAdvice
class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(UnbalancedTransactionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	Map<String, String> handleException(UnbalancedTransactionException e){
		return Collections.singletonMap("message", e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	Map<String, String> handleException(Exception e){
		return Collections.singletonMap("message", e.getMessage());
	}
	
}

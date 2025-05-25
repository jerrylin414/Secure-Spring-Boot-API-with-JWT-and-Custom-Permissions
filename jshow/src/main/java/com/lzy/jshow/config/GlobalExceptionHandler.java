package com.lzy.jshow.config;


import com.lzy.jshow.constants.ResponseCode;
import com.lzy.jshow.constants.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler
	@ResponseBody
	public ResponseCode handle(Exception exception, HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
	{
		String className = handlerMethod.getMethod().getDeclaringClass().toString();
		String requestLog = "Error Class " + className + ": " + exception.getMessage();
		response.setStatus(200);
		
		if(exception instanceof SQLException){
		}
		else if(exception instanceof SQLDataException){
		}
		else if(exception instanceof AccessDeniedException){
			response.setStatus(403);
		}
		else if(exception instanceof HttpRequestMethodNotSupportedException){
		}
		else if(exception instanceof MethodArgumentNotValidException){
			//MethodArgumentNotValidException exception6 = (MethodArgumentNotValidException) exception;
			//String errorField = exception6.getBindingResult().getFieldError().getField();
			//requestLog = "Error Class " + className + ": " + exception.getMessage() + ", errorField: " + errorField;
			
			Map<String, String> errors = new HashMap<>();
			((MethodArgumentNotValidException) exception).getBindingResult().getAllErrors().forEach((error) -> {
	            String fieldName = ((FieldError) error).getField();
	            String errorMessage = error.getDefaultMessage();
	            errors.put(fieldName, errorMessage);
	        });
			requestLog = "Error Class " + className + ": " 
					+ errors.entrySet().stream().map(entry -> entry.getKey() + " -> " + entry.getValue()).collect(Collectors.joining(", "));
		}
		else if(exception instanceof HttpMessageNotReadableException){
		}
		else if(exception instanceof ConnectException){
		}
		else if(exception instanceof MissingServletRequestParameterException){
		}
		else if(exception instanceof RejectedExecutionException){
		}
		else if(exception instanceof NullPointerException){
		}
		else{
			response.setStatus(400);
		}
		
		StringWriter errorWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(errorWriter));
		logger.error("Error occurred: {}" , (errorWriter.toString()) );

		return new ResponseCode(ResultCode.REQUEST_ERROR, requestLog);
	}
}

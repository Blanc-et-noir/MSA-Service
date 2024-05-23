package com.spring.api.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.spring.api.code.Code;
import com.spring.api.code.DefaultServiceCode;
import com.spring.api.exception.CustomException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({CustomException.class})
	public ModelAndView handleCustomException(CustomException e){
		Code code = e.getCode();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/error");
		modelAndView.addObject("code", code.getCode());
		modelAndView.addObject("message", code.getMessage());
		modelAndView.addObject("status", code.getStatus());
		
		return modelAndView;
	}
	
	@ExceptionHandler({Exception.class})
	public ModelAndView handleException(Exception e) {
		Code code = DefaultServiceCode.INTERNAL_SERVER_ERROR;
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/error");
		modelAndView.addObject("code", code.getCode());
		modelAndView.addObject("message", code.getMessage());
		modelAndView.addObject("status", code.getStatus());
		
		return modelAndView;
	}
}
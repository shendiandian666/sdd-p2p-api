/*package com.sdd.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

//@ControllerAdvice(annotations = {Controller.class})
public class GlobalExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "/error";
	
	@ExceptionHandler(Exception.class)
    public ModelAndView error500Page(HttpServletRequest req, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("/500");
        return mav;
    }
	
	
}
*/
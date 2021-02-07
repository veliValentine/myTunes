package group.demo.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

public class ExceptionController {
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404Exception(){
        return "error";
    }
}

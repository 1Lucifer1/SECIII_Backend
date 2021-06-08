package team.software.irbl.util;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrHandler {

    @ExceptionHandler(Err.class)
    public Res errHandler(Err e) {
        return Res.failure(e.msg);
    }

}

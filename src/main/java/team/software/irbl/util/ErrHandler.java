package team.software.irbl.util;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrHandler {

    @ExceptionHandler(Err.class)
    public team.software.irbl.util.Res errHandler(Err e) {
        return team.software.irbl.util.Res.failure(e.msg);
    }

}

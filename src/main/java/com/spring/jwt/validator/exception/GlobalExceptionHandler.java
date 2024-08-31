package com.spring.jwt.validator.exception;

import com.spring.jwt.validator.model.DTO.ErrorDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler {
    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleStudentNotFoundException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("error while authenticate, please verify bearer token, " + ex.getLocalizedMessage());
        errorDto.setStacktrace("follow the white rabbit ----- " + Arrays.toString(ex.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorDto);
    }



}
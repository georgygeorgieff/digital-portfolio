package com.example.digitalportfolio.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler
{
  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ExceptionDto emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException e)
  {
    return new ExceptionDto("Not found");
  }

  @ExceptionHandler(IllegalDataException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ExceptionDto illegalDataExceptionExceptionHandler(IllegalDataException e)
  {

    return new ExceptionDto(e.getMessage());
  }

  @ExceptionHandler({AccessForbiddenException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ExceptionDto accessForbiddenExceptionHandler(AccessForbiddenException e)
  {
    return new ExceptionDto(e.getMessage());
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ResponseBody
  public ExceptionDto constraintViolationExceptionHandler(ConstraintViolationException e1)
  {
    return new ExceptionDto(e1.getMessage());
  }


}

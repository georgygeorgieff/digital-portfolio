package com.example.digitalportfolio.exception;


public class AccessForbiddenException extends RuntimeException
{
  public AccessForbiddenException(String message)
  {
    super(message);
  }

  public AccessForbiddenException(String message, Throwable cause)
  {
    super(message, cause);
  }
}

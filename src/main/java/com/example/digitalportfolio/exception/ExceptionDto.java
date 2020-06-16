package com.example.digitalportfolio.exception;

import java.time.LocalDateTime;

public class ExceptionDto
{

  private       String        message;
  private final LocalDateTime timestamp;

  public ExceptionDto()
  {
    timestamp = LocalDateTime.now();
  }

  public ExceptionDto(String message)
  {
    this();
    this.message = message;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public LocalDateTime getTimestamp()
  {
    return timestamp;

  }
}

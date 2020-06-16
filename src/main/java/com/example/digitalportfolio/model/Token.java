package com.example.digitalportfolio.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Token
{
  private String        token;
  private LocalDateTime timestamp;
  private String        email;

  public Token()
  {
    this.timestamp = LocalDateTime.now();
    this.token = UUID.randomUUID().toString();
  }

  public String getToken()
  {
    return token;
  }

  public void setToken(String token)
  {
    this.token = token;
  }

  public LocalDateTime getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }


}

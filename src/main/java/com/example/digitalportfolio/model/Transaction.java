package com.example.digitalportfolio.model;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Transaction
{
  @Id
  private long          id;
  private String        sender;
  private String        receiver;
  private BigDecimal    money;
  private LocalDateTime localDateTime;

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getSender()
  {
    return sender;
  }

  public void setSender(String sender)
  {
    this.sender = sender;
  }

  public String getReceiver()
  {
    return receiver;
  }

  public void setReceiver(String receiver)
  {
    this.receiver = receiver;
  }

  public BigDecimal getMoney()
  {
    return money;
  }

  public void setMoney(BigDecimal money)
  {
    this.money = money;
  }

  public LocalDateTime getLocalDateTime()
  {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime)
  {
    this.localDateTime = localDateTime;
  }
}

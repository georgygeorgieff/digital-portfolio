package com.example.digitalportfolio.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionDto
{
  private String     sender;
  private String     receiver;
  private BigDecimal money;
  private Timestamp  localDateTime;

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

  public Timestamp getLocalDateTime()
  {
    return localDateTime;
  }

  public void setLocalDateTime(Timestamp localDateTime)
  {
    this.localDateTime = localDateTime;
  }
}

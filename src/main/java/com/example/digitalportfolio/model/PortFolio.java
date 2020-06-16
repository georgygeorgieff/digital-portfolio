package com.example.digitalportfolio.model;

import java.math.BigDecimal;

public class PortFolio
{
  private BigDecimal money;
  private String     username;

  public BigDecimal getMoney()
  {
    return money;
  }

  public void setMoney(BigDecimal money)
  {
    this.money = money;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }
}

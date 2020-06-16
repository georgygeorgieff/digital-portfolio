package com.example.digitalportfolio.validator;

import com.example.digitalportfolio.exception.IllegalDataException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MoneyValidator
{

  public void validateHasEnoughMoney(BigDecimal moneyForPurchase, BigDecimal userMoney)
  {
    if (userMoney.compareTo(moneyForPurchase) > 0) {
      throw new IllegalDataException("Not enough money!");
    }
  }

  public void validateBigDecimalValueIsPositive(BigDecimal value)
  {
    if (value.signum() != 1) {
      throw new IllegalDataException("The value cannot be negative!");
    }
  }

  public void validateEqualMoney(BigDecimal money, BigDecimal money2)
  {
    if (money.compareTo(money2) != 0) {
      throw new IllegalDataException("The amount of money are not equal!");
    }
  }
}

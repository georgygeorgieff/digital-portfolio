package com.example.digitalportfolio.validator;

import com.example.digitalportfolio.exception.AccessForbiddenException;
import com.example.digitalportfolio.exception.IllegalDataException;
import com.example.digitalportfolio.model.User;
import com.example.digitalportfolio.role.Roles;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;

@Component
public class Validator
{
  public void validateUserIsConsumer(User user)
  {
    if (!user.getRole().contains(Roles.CONSUMER_ROLE)) {
      throw new IllegalDataException("This user is not consumer!");
    }
  }

  public void validateBuyerIsNotAdmin(String userRole)
  {
    if (userRole.equalsIgnoreCase(Roles.ADMIN_ROLE)) {
      throw new IllegalDataException("This user is Admin!");
    }
  }


  public void validateEqualStrings(String string1, String string2)
  {
    if (!string1.equals(string2)) {
      throw new IllegalDataException("These strings are not equal!");
    }
  }

  public void validateIsNotNull(Object o)
  {
    if (o != null) {
      throw new IllegalDataException("This object exists!");
    }
  }

  public void validateIsNull(Object o)
  {
    if (o == null) {
      throw new AccessForbiddenException("Empty data !");
    }
  }


}

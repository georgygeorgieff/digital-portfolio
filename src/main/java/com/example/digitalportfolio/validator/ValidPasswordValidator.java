package com.example.digitalportfolio.validator;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String>
{
  @Override
  public void initialize(ValidPassword arg0)
  {
  }

  /**
   * Retrieves an information that is the password valid.
   * There are some requirements.
   * - Length have to be minimum 8 characters, maximum 45 characters.
   * - There must have 2 upper cased characters
   * - There must have at least 1 digit character
   * - Whitespaces are not allowed.
   * if the password pass those requirements, it is valid.
   *
   * @return boolean
   */

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context)
  {
    PasswordValidator validator = new PasswordValidator(Arrays.asList(
        new LengthRule(8, 45),
        new UppercaseCharacterRule(2),
        new DigitCharacterRule(1),
        new SpecialCharacterRule(1),
        new QwertySequenceRule(5, false),
        new WhitespaceRule()));

    RuleResult result = validator.validate(new PasswordData(password));
    if (result.isValid()) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(String.valueOf(validator.getMessages(result)))
        .addConstraintViolation();
    return false;
  }
}

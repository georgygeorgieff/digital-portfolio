package com.example.digitalportfolio.service;

import com.example.digitalportfolio.exception.IllegalDataException;
import com.example.digitalportfolio.model.PortFolio;
import com.example.digitalportfolio.repository.PortFolioRepository;
import com.example.digitalportfolio.model.User;
import com.example.digitalportfolio.repository.TokenRepository;
import com.example.digitalportfolio.repository.UserRepository;
import com.example.digitalportfolio.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;

@Service
public class UserService
{
  private UserRepository      userRepository;
  private PortFolioRepository portFolioRepository;
  private Validator           validator;
  private TokenService        tokenService;


  public UserService()
  {
  }

  @Autowired
  public UserService(UserRepository userRepository, PortFolioRepository portFolioRepository,
                     Validator validator, TokenService tokenService)
  {
    this.userRepository = userRepository;
    this.portFolioRepository = portFolioRepository;
    this.validator = validator;
    this.tokenService = tokenService;
  }

  /**
   * That's a method wherewith user is created.
   * There is a check if user is null, if it is, an exception is thrown.
   * There is a check if the role is admin, if it is , an exception is thrown.
   * There is a check if the username is 'admin', if it is, an exception is thrown.
   *
   * @param userName the username which is necessary for logging.
   * @param password the password which is necessary for logging.
   * @param role     the role which the user has.
   * @param email    the email for receiving notifications.
   */

  public void createUser(String userName, @Valid String password, String role, String email)
  {
    User u = userRepository.findUserPerRegistration(userName);
    User u1 = userRepository.findEmailPerRegistration(email);

    validator.validateIsNotNull(u);
    validator.validateIsNotNull(u1);
    validator.validateIsNull(userName);
    validator.validateIsNull(role);
    validator.validateIsNull(email);
    validator.validateBuyerIsNotAdmin(role);
    validateAdminName(userName);
    userRepository.createUser(userName, password, email);
    tokenService.createToken(email);
  }


  /**
   * That's a method wherewith user get it's balance in portfolio.
   *
   * @param username the username which is necessary for logging.
   */

  public BigDecimal getBalance(String username)
  {
    PortFolio p = portFolioRepository.findByUserName(username);
    return p.getMoney();
  }

  public User findByUserName(String username)
  {
    return userRepository.findByUserName(username);
  }

  public User findAdmin(String username)
  {
    return userRepository.findByUserName(username);
  }

  public void delete(String username)
  {
    userRepository.delete(username);
  }

  public void enableUser(String username)//for testing
  {
    userRepository.enableUser(username);
  }

  private void validateAdminName(String name1)
  {
    if (name1.equalsIgnoreCase("admin")) {
      throw new IllegalDataException("The username cannot be 'admin'!");
    }
  }
}

package com.example.digitalportfolio.controller;

import com.example.digitalportfolio.dto.TransactionDto;
import com.example.digitalportfolio.service.*;

import com.example.digitalportfolio.validator.ValidPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController
{

  private UserService        userService;
  private PortFolioService   portFolioService;
  private TransactionService transactionService;
  private RoleService        roleService;
  private MailService        mailService;
  private TokenService       tokenService;

  @Autowired
  public UserController(UserService userService, PortFolioService portFolioService,
                        TransactionService transactionService, RoleService roleService,
                        MailService mailService, TokenService tokenService)
  {
    this.userService = userService;
    this.portFolioService = portFolioService;
    this.transactionService = transactionService;
    this.roleService = roleService;
    this.mailService = mailService;
    this.tokenService = tokenService;
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestParam(name = "username") String username,
                                         @RequestParam(name = "password") @ValidPassword @Valid String password,
                                         @RequestParam(name = "role") String role,
                                         @RequestParam(name = "email") String email)
  {
    userService.createUser(username, new BCryptPasswordEncoder().encode(password), role, email);
    roleService.addRole(role, username);
    portFolioService.create(username);
    mailService.sendWelcomeMessage(email, username);
    return ResponseEntity.ok().build();

  }

  /**
   * That's a method wherewith user can check his balance in portfolio using /api/v1/user/balance
   *
   * @return ResponseEntity<BigDecimal>
   */
  @GetMapping("/confirm")
  @PostMapping("/confirm")
  //@RequestMapping(value = "/confirm", method = { RequestMethod.POST,  RequestMethod.GET })
  public ResponseEntity<Void> confirmUserRegistration(@RequestParam("token") String token)
  {
    tokenService.confirmUserRegistration(token);
    return ResponseEntity.ok().build();


  }

  @GetMapping("/balance")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).CONSUMER_ROLE) or " +
      "hasRole(T(com.example.digitalportfolio.role.Roles).MERCHANT_ROLE)")
  public ResponseEntity<BigDecimal> getBalance(Principal p)
  {
    return ResponseEntity.ok(userService.getBalance(p.getName()));
  }

  /**
   * That's a method wherewith user can see his payment history using /api/v1/user/history
   *
   * @param count the number of payments which have to be displayed.
   * @return ResponseEntity<List><TransactionDto>
   */

  @GetMapping("/history")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).CONSUMER_ROLE) or " +
      "hasRole(T(com.example.digitalportfolio.role.Roles).MERCHANT_ROLE)")
  public ResponseEntity<List<TransactionDto>> history(@RequestParam(value = "count", required = false, defaultValue = "-1")
                                                          int count,
                                                      Principal p)
  {
    List<TransactionDto> transactions = transactionService.findByUserName(count, p.getName());

    return ResponseEntity.ok(transactions);
  }


}


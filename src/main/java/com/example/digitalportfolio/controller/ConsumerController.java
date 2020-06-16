package com.example.digitalportfolio.controller;

import com.example.digitalportfolio.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class ConsumerController
{
  private ConsumerService consumerService;

  @Autowired
  public ConsumerController(ConsumerService consumerService)
  {
    this.consumerService = consumerService;
  }

  /**
   * That's a method wherewith consumer can add bank card using /api/v1/user/card
   *
   * @param number
   * @return ResponseEntity
   */

  @PostMapping("/card")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).CONSUMER_ROLE)")
  public ResponseEntity<Void> addBankCard(@RequestParam("number") String number,
                                          Principal p)
  {
    consumerService.addBankCard(number, p.getName());
    return ResponseEntity.ok().build();
  }

  /**
   * That's a method wherewith consumer can transfer money to consumer user using /api/v1/user/transfer
   *
   * @param money    the value of money which have ot be transferred.
   * @param username the username which user can log into database.
   * @return ResponseEntity
   */

  @PostMapping("/transfer")
  @Transactional
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).CONSUMER_ROLE)")
  public ResponseEntity<Void> transfer(@RequestParam("money") BigDecimal money,
                                       @RequestParam("username") String username,
                                       Principal p)
  {
    consumerService.transfer(money, username, p.getName());
    return ResponseEntity.ok().build();
  }
}


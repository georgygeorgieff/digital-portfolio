package com.example.digitalportfolio.controller;

import com.example.digitalportfolio.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;


@RestController
@RequestMapping("/api/v1/user")
public class MerchantController
{
  private MerchantService merchantService;

  @Autowired
  public MerchantController(MerchantService merchantService)
  {
    this.merchantService = merchantService;
  }

  /**
   * That's a method wherewith merchant can get enough money for the purchase from consumer using /api/v1/user/getmoney
   *
   * @param money    the value of money
   * @param username the username which user can log into database.
   * @return ResponseEntity
   */

  @PostMapping("/getmoney")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).MERCHANT_ROLE)")
  @Transactional
  public ResponseEntity<Void> getMoneyForPurchase(@RequestParam("money") BigDecimal money,
                                                  @RequestParam("username") String username,
                                                  Principal p)
  {
    merchantService.getMoneyForPurchase(money, username, p.getName());
    return ResponseEntity.ok().build();

  }

  /**
   * That's a method wherewith merchant can add a bank account from which he can withdraw stored money in his portfolio
   * using /api/v1/user/account
   *
   * @param number
   * @return ResponseEntity
   */

  @PostMapping("/account")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).MERCHANT_ROLE)")
  public ResponseEntity<Void> addBankAccount(@RequestParam("number") String number,
                                             Principal p)
  {
    merchantService.addBankAccount(number, p.getName());
    return ResponseEntity.ok().build();
  }

  /**
   * That's a method wherewith merchant can transfer the stored money from portfolio to bank account
   * using /api/v1/user/setlock
   *
   * @param number the bank account number
   * @param money  the value of money
   * @return ResponseEntity
   */

  @PostMapping("/tobankaccount")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).MERCHANT_ROLE)")
  @Transactional
  public ResponseEntity<Void> transferToBankAccount(@RequestParam("number") String number,
                                                    BigDecimal money,
                                                    Principal p)
  {
    merchantService.transferToBankAccount(number, money, p.getName());
    return ResponseEntity.ok().build();
  }

  /**
   * That's a method wherewith merchant can return money to consumer using /api/v1/user/backmoney
   *
   * @param money    the value of money
   * @param username the username which user can log into database.
   * @return ResponseEntity
   */

  @PostMapping("/backmoney")
  @PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).MERCHANT_ROLE)")
  @Transactional
  ResponseEntity<Void> backMoneyForPurchase(@RequestParam("money") BigDecimal money,
                                            @RequestParam("username") String username,
                                            Principal p)
  {
    merchantService.backMoneyForPurchase(money, username, p.getName());
    return ResponseEntity.ok().build();
  }


}

package com.example.digitalportfolio.service;

import com.example.digitalportfolio.dto.TransactionDto;
import com.example.digitalportfolio.exception.IllegalDataException;
import com.example.digitalportfolio.model.BankAccount;
import com.example.digitalportfolio.model.PortFolio;
import com.example.digitalportfolio.model.User;
import com.example.digitalportfolio.repository.BankAccountRepository;
import com.example.digitalportfolio.repository.PortFolioRepository;
import com.example.digitalportfolio.repository.TransactionRepository;
import com.example.digitalportfolio.repository.UserRepository;
import com.example.digitalportfolio.validator.MoneyValidator;
import com.example.digitalportfolio.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MerchantService
{

  private UserRepository        userRepository;
  private PortFolioRepository   portFolioRepository;
  private TransactionRepository transactionRepository;
  private BankAccountRepository bankAccountRepository;
  private Validator             validator;
  private MoneyValidator        moneyValidator;
  private MailService           mailService;


  @Autowired
  public MerchantService(UserRepository userRepository, PortFolioRepository portFolioRepository,
                         TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository,
                         Validator validator, MailService mailService,
                         MoneyValidator moneyValidator)
  {
    this.userRepository = userRepository;
    this.portFolioRepository = portFolioRepository;
    this.transactionRepository = transactionRepository;
    this.bankAccountRepository = bankAccountRepository;
    this.validator = validator;
    this.mailService = mailService;
    this.moneyValidator = moneyValidator;
  }

  /**
   * That's a method wherewith merchant can get money from consumer.
   * There is a check if receiver is consumer, if it not, an exception is thrown.
   * There is a check if sender has enough money, if it has not, an exception is thrown.
   * There is a check if the value of money is positive, if it is not, an exception is thrown.
   * If those validations pass positively, the merchant has it's money in his portfolio.
   * There is an option for email notification, but at first the user have to set his email
   * into it's user information.
   *
   * @param moneyForPurchase the value of money
   * @param username         the username which is necessary for logging about consumer.
   * @param myName           the username which is necessary for logging about merchant.
   */

  public void getMoneyForPurchase(BigDecimal moneyForPurchase,
                                  String username,
                                  String myName)
  {
    User user = userRepository.findByUserName(username);

    PortFolio userPortFolio = portFolioRepository.findByUserNameForUpdate(user.getUsername());
    PortFolio myPortFolio = portFolioRepository.findByUserNameForUpdate(myName);

    validator.validateUserIsConsumer(user);
    moneyValidator.validateHasEnoughMoney(userPortFolio.getMoney(), moneyForPurchase);
    moneyValidator.validateBigDecimalValueIsPositive(moneyForPurchase);

    portFolioRepository.update(myName, myPortFolio.getMoney().add(moneyForPurchase));
    portFolioRepository.update(user.getUsername(), userPortFolio.getMoney().subtract(moneyForPurchase));
    transactionRepository.save(user.getUsername(), myName, moneyForPurchase, Timestamp.valueOf(LocalDateTime.now()));
    User me = userRepository.findByUserName(myName); //to get email;
    mailService.sendPaymentReceiverMessage(me.getEmail(), username);
    mailService.sendPaymentSenderMessage(user.getEmail(), me.getUsername());
  }

  /**
   * Merchant can add bank account to his portfolio.
   *
   * @param number the number of bank account.
   * @param myName the username which is necessary for logging.
   */

  public void addBankAccount(String number,
                             String myName)
  {
    bankAccountRepository.save(number, myName);
  }

  /**
   * That's a method wherewith merchant can transfer money to bankaccount.
   * There is a check if the merchant has enough money, if it has not, an exception is thrown.
   * If those validations pass positively, the merchant has it's money in his bank account.
   * There is an option for email notification, but at first the user have to set his email
   * into it's user information.
   *
   * @param number the number of bank account
   * @param money  the value of money
   * @param myName the username which is necessary for logging about merchant.
   */

  public void transferToBankAccount(String number,
                                    BigDecimal money,
                                    String myName)
  {
    PortFolio p = portFolioRepository.findByUserNameForUpdate(myName);
    BankAccount b = bankAccountRepository.findByNumber(number, myName);

    validator.validateEqualStrings(b.getUsername(), myName);
    moneyValidator.validateHasEnoughMoney(p.getMoney(), money);
    moneyValidator.validateBigDecimalValueIsPositive(money);

    BigDecimal myMoney = p.getMoney().subtract(money);
    portFolioRepository.update(myName, myMoney);
    transactionRepository.save(myName, myName, money, Timestamp.valueOf(LocalDateTime.now()));
    User me = userRepository.findByUserName(myName); //to get email;
    mailService.sendDepositOrSendMoneyToBankAccountMessage(me.getEmail(), me.getUsername());
  }

  /**
   * That's a method wherewith merchant can return money to consumer.
   * It searches the transaction list, for the exact transaction.
   * There is a check if the sender is the same.
   * There is a check if the receiver is the same.
   * There is a check if the value of money is the same.
   * If those validations pass positively, the consumer has it's money in his portfolio.
   * There is an option for email notification, but at first the user have to set his email
   * into it's user information.
   *
   * @param moneyToBack the value of money
   * @param username    the username which is necessary for logging about consumer.
   * @param myName      the username which is necessary for logging about merchant.
   */

  public void backMoneyForPurchase(BigDecimal moneyToBack, String username,
                                   String myName)
  {
    User u = userRepository.findByUserName(username);

    List<TransactionDto> t = transactionRepository.findBySenderAndReceiverAndMoney(u.getUsername(),
        myName,
        moneyToBack);
    validateCollectionIsEmpty(t);
    TransactionDto t1 = t.get(0);

    validator.validateEqualStrings(t1.getSender(), u.getUsername());
    validator.validateEqualStrings(t1.getReceiver(), myName);
    moneyValidator.validateEqualMoney(t1.getMoney(), moneyToBack);

    PortFolio myPortFolio = portFolioRepository.findByUserNameForUpdate(myName);

    moneyValidator.validateHasEnoughMoney(myPortFolio.getMoney(), moneyToBack);

    PortFolio userPortFolio = portFolioRepository.findByUserNameForUpdate(u.getUsername());
    BigDecimal myMoneyAfter = myPortFolio.getMoney().subtract(moneyToBack);
    BigDecimal userMoneyAfter = userPortFolio.getMoney().add(moneyToBack);
    portFolioRepository.update(myName, myMoneyAfter);
    portFolioRepository.update(u.getUsername(), userMoneyAfter);
    transactionRepository.save(myName, u.getUsername(), moneyToBack, Timestamp.valueOf(LocalDateTime.now()));
    User me = userRepository.findByUserName(myName); //to get email;
    mailService.sendPaymentReceiverMessage(u.getEmail(), me.getUsername());
    mailService.sendPaymentSenderMessage(me.getEmail(), u.getUsername());
  }

  private void validateCollectionIsEmpty(Collection c)
  {
    if (c.isEmpty()) {
      throw new IllegalDataException("This transaction do not exists!");
    }
  }


}

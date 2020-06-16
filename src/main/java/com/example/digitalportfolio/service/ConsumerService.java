package com.example.digitalportfolio.service;


import com.example.digitalportfolio.model.PortFolio;
import com.example.digitalportfolio.model.User;
import com.example.digitalportfolio.repository.BankCardRepository;
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

@Service
public class ConsumerService
{

  private UserRepository        userRepository;
  private TransactionRepository transactionRepository;
  private PortFolioRepository   portFolioRepository;
  private BankCardRepository    bankCardRepository;
  private Validator             validator;
  private MailService           mailService;
  private MoneyValidator        moneyValidator;


  @Autowired
  public ConsumerService(UserRepository userRepository, TransactionRepository transactionRepository,
                         PortFolioRepository portFolioRepository,
                         BankCardRepository bankCardRepository, Validator validator,
                         MailService mailService, MoneyValidator moneyValidator)
  {
    this.userRepository = userRepository;
    this.transactionRepository = transactionRepository;
    this.portFolioRepository = portFolioRepository;
    this.bankCardRepository = bankCardRepository;
    this.validator = validator;
    this.mailService = mailService;
    this.moneyValidator = moneyValidator;
  }

  /**
   * That's a method wherewith consumer can transfer money to other consumer.
   * There is a check if sender has enough money, if it has not, an exception is thrown.
   * There is a check if receiver is consumer, if it not, an exception is thrown.
   * There is a check if the value of money is positive, if it is not, an exception is thrown.
   * If those validations pass positively, the receiver has it's money in his portfolio.
   * There is an option for email notification, but at first the user have to set his email
   * into it's user information.
   *
   * @param username the username which is necessary for logging.
   */

  public void transfer(BigDecimal moneyForTransfer,
                       String username,
                       String myUsername)
  {
    User receiver = userRepository.findByUserName(username);
    PortFolio myPortFolio = portFolioRepository.findByUserNameForUpdate(myUsername);

    moneyValidator.validateHasEnoughMoney(myPortFolio.getMoney(), moneyForTransfer);
    validator.validateUserIsConsumer(receiver);
    moneyValidator.validateBigDecimalValueIsPositive(moneyForTransfer);

    BigDecimal myMoney = myPortFolio.getMoney().subtract(moneyForTransfer);
    PortFolio receiverPortFolio = portFolioRepository.findByUserNameForUpdate(receiver.getUsername()); // it`s receiver need to be a lock ??
    BigDecimal receiverMoney = receiverPortFolio.getMoney().add(moneyForTransfer);
    portFolioRepository.update(myUsername, myMoney);
    portFolioRepository.update(receiver.getUsername(), receiverMoney);
    transactionRepository.save(myUsername, receiver.getUsername(), moneyForTransfer, Timestamp.valueOf(LocalDateTime.now()));
    User me = userRepository.findByUserName(myUsername); //to get email;
    mailService.sendPaymentReceiverMessage(receiver.getEmail(), me.getUsername());
    mailService.sendPaymentSenderMessage(me.getEmail(), receiver.getUsername());
  }

  /**
   * User can add bank card to his portfolio.
   *
   * @param number the number of bank card.
   * @param myName the username which is necessary for logging.
   */
  public void addBankCard(String number,
                          String myName)
  {
    bankCardRepository.save(number, myName);
  }

}



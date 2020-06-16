package com.example.digitalportfolio.service;

import com.example.digitalportfolio.model.Token;
import com.example.digitalportfolio.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/*
 * if you want to send email , change your google account less security to enable
 * https://myaccount.google.com/lesssecureapps
 * */

@Service
@Component
public class MailService
{
  private       MailSender      mailSender;
  private       TokenRepository tokenRepository;
  private final String          WELCOME_SUBJECT          = "Welcome to digital portfolio";
  private final String          PAYMENT_SUBJECT          = "New action in your digital portfolio account.";
  private final String          PAYMENT_SENDER_MESSAGE   = "Your transaction to %s is  successful.";
  private final String          PAYMENT_RECEIVER_MESSAGE = "You have new transaction from %s.";


  @Autowired
  public MailService(MailSender mailSender, TokenRepository tokenRepository)
  {
    this.mailSender = mailSender;
    this.tokenRepository = tokenRepository;

  }

  /**
   * That's a method wherewith a welcome message is sent via email.
   * There is a check if the email is valid, if it is not an exception is thrown.
   *
   * @param email    the user's email.
   * @param username the username which is necessary for logging.
   */

  public void sendWelcomeMessage(String email, String username)
  {
    Token t = tokenRepository.findUserTokenByEmail(email);
    String welcomeTextAndConfirmation = "Dear %s ,please in next 12 hours confirm your account,to :" +
        " http://localhost:8080/api/v1/user/confirm?token=" + t.getToken();
    doSendMessage(email, username, WELCOME_SUBJECT, welcomeTextAndConfirmation);
  }

  /**
   * That's a method wherewith a send-payment message is sent via email.
   * There is a check if the email is valid, if it is not an exception is thrown.
   *
   * @param email    the user's email.
   * @param username the username which is necessary for logging.
   */


  public void sendPaymentSenderMessage(String email, String username)
  {
    doSendMessage(email, username, PAYMENT_SUBJECT, PAYMENT_SENDER_MESSAGE);
  }

  /**
   * That's a method wherewith a payment receive message is sent via email.
   * There is a check if the email is valid, if it is not an exception is thrown.
   *
   * @param email    the user's email.
   * @param username the username which is necessary for logging.
   */


  public void sendPaymentReceiverMessage(String email, String username)
  {
    doSendMessage(email, username, PAYMENT_SUBJECT, PAYMENT_RECEIVER_MESSAGE);

  }

  /**
   * That's a method wherewith a deposit message is sent via email.
   * There is a check if the email is valid, if it is not an exception is thrown.
   *
   * @param email    the user's email.
   * @param username the username which is necessary for logging.
   */

  public void sendDepositOrSendMoneyToBankAccountMessage(String email, String username)
  {
    doSendMessage(email, username, PAYMENT_SUBJECT, PAYMENT_RECEIVER_MESSAGE);

  }

  private void doSendMessage(String email, String username, String subject, String text)
  {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(email);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(
        String.format(text, username));
    mailSender.send(simpleMailMessage);
  }

}



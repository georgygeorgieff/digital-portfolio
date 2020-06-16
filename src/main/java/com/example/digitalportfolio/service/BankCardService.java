package com.example.digitalportfolio.service;

import com.example.digitalportfolio.model.BankCard;
import com.example.digitalportfolio.repository.BankCardRepository;
import com.example.digitalportfolio.repository.UserRepository;
import com.example.digitalportfolio.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankCardService
{

  private BankCardRepository bankCardRepository;
  private UserRepository     userRepository;

  @Autowired
  public BankCardService(UserRepository userRepository, BankCardRepository bankCardRepository)
  {
    this.userRepository = userRepository;
    this.bankCardRepository = bankCardRepository;
  }

  public void save(String number, String username)
  {
    bankCardRepository.save(number, username);
  }

  public BankCard findByNumber(String number, String userName)
  {
    return bankCardRepository.findByNumber(number, userName);
  }

  public void delete(String username, String number)
  {
    bankCardRepository.delete(username, number);
  }

}

package com.example.digitalportfolio.service;

import com.example.digitalportfolio.model.BankAccount;
import com.example.digitalportfolio.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService
{
  BankAccountRepository bankAccountRepository;

  @Autowired
  public BankAccountService(BankAccountRepository bankAccountRepository)
  {
    this.bankAccountRepository = bankAccountRepository;
  }

  public void save(String number, String username)
  {
    bankAccountRepository.save(number, username);
  }

  public BankAccount findByNumber(String number, String username)
  {
    return bankAccountRepository.findByNumber(number, username);
  }

  public void delete(String username, String number)
  {
    bankAccountRepository.delete(username, number);
  }
}

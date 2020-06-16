package com.example.digitalportfolio.service;

import com.example.digitalportfolio.dto.TransactionDto;
import com.example.digitalportfolio.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionService
{
  private TransactionRepository transactionRepository;

  @Autowired
  public TransactionService(TransactionRepository transactionRepository)
  {
    this.transactionRepository = transactionRepository;
  }

  public void save(String sender, String receiver, BigDecimal money, Timestamp localDateTime)
  {
    transactionRepository.save(sender, receiver, money, localDateTime);
  }

  public List<TransactionDto> findByUserName(int count, String username)
  {
    return transactionRepository.findByUserName(count, username);
  }

  public List<TransactionDto> findBySenderAndReceiverAndMoney(String sender, String receiver, BigDecimal money)
  {
    return transactionRepository.findBySenderAndReceiverAndMoney(sender, receiver, money);
  }

  public void delete(String sender, String receiver)
  {
    transactionRepository.delete(sender, receiver);
  }
}

package com.example.digitalportfolio.service;

import com.example.digitalportfolio.model.PortFolio;
import com.example.digitalportfolio.repository.PortFolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PortFolioService
{
  private PortFolioRepository portFolioRepository;

  @Autowired
  public PortFolioService(PortFolioRepository portFolioRepository)
  {
    this.portFolioRepository = portFolioRepository;
  }

  public void create(String username)
  {
    portFolioRepository.save(username);
  }

  public PortFolio findByUserName(String username)
  {
    return portFolioRepository.findByUserName(username);
  }

  public void update(String username, BigDecimal money)
  {
    portFolioRepository.update(username, money);
  }

  public void delete(String username)
  {
    portFolioRepository.delete(username);
  }
}

package com.example.digitalportfolio.service;

import com.example.digitalportfolio.model.Token;
import com.example.digitalportfolio.model.User;
import com.example.digitalportfolio.repository.TokenRepository;
import com.example.digitalportfolio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Component
public class TokenService
{
  private TokenRepository tokenRepository;
  private UserRepository  userRepository;
  private MailService     mailService;

  @Autowired
  public TokenService(TokenRepository tokenRepository, UserRepository userRepository,
                      MailService mailService)
  {
    this.tokenRepository = tokenRepository;
    this.userRepository = userRepository;
    this.mailService = mailService;
  }

  public void createToken(String email)
  {
    Token t = new Token();
    t.setEmail(email);
    tokenRepository.createToken(t.getToken(), Timestamp.valueOf(t.getTimestamp()), t.getEmail());
  }

  public Token getUserToken(String email)
  {
    return tokenRepository.findUserTokenByEmail(email);
  }

  public void confirmUserRegistration(String token)
  {
    Token t = tokenRepository.findUserTokenByTokenNumber(token);
    User u = userRepository.findByEmail(t.getEmail());
    tokenRepository.deleteTokenByToken(token);
    if (!isExpiredToken(t.getTimestamp())) {
      userRepository.enableUser(u.getUsername());
    }
    else {
      createToken(u.getEmail());
      mailService.sendWelcomeMessage(u.getEmail(), u.getUsername());
    }
  }

  private boolean isExpiredToken(LocalDateTime tokenTime)
  {
    return !tokenTime.plusHours(12).isAfter(LocalDateTime.now());
  }

  public void deleteByEmail(String email)
  {
    tokenRepository.deleteTokenByEmail(email);
  }

  public void deleteByToken(String token)
  {
    tokenRepository.deleteTokenByToken(token);
  }
}

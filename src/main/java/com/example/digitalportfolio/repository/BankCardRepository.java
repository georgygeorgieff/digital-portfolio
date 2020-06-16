package com.example.digitalportfolio.repository;

import com.example.digitalportfolio.model.BankCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;


@Repository
public class BankCardRepository
{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public BankCardRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * That's a method wherewith information about bank card is stored into sql table.
   * using namedParameterJdbcTemplate.
   *
   * @param number   the bank account number
   * @param username
   */

  public void save(String number, String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("number", number);
    mapSqlParameterSource.addValue("username", username);
    String sql = "INSERT INTO BANKCARD(username, number) VALUES (:username,:number);";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * Retrieves an information about user's bank card.
   *
   * @param number   the bank account number
   * @param username
   * @return an object of BankCard.
   */

  public BankCard findByNumber(String number, String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("number", number);
    mapSqlParameterSource.addValue("username", username);
    String sql = "SELECT NUMBER, USERNAME FROM BANKCARD WHERE NUMBER = :number AND USERNAME = :username;";

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
        (ResultSet rs, int rowNums) -> {
          BankCard b1 = new BankCard();
          b1.setUsername(rs.getString("username"));
          b1.setNumber(rs.getString("number"));
          return b1;

        });
  }

  /**
   * That's a method wherewith information about bank account can be deleted.
   *
   * @param username
   * @param number   the bank account number
   */


  public void delete(String username, String number)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("number", number);
    mapSqlParameterSource.addValue("username", username);
    String sql = "DELETE FROM BANKCARD WHERE USERNAME = :username AND NUMBER = :number;";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

}

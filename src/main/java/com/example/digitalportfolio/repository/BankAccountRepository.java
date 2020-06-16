package com.example.digitalportfolio.repository;

import com.example.digitalportfolio.model.BankAccount;
import com.example.digitalportfolio.model.BankCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

@Repository
public class BankAccountRepository
{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


  @Autowired
  public BankAccountRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

  }

  /**
   * That's a method wherewith information about bank account is stored into sql table
   * using namedParameterJdbcTemplate.
   *
   * @param number   the bank account number
   * @param username
   */

  public void save(String number, String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("number", number);
    String sql = "INSERT INTO BANKACCOUNT(NUMBER,USERNAME) VALUES (:number, :username);";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * Retrieves an information about user's bank account.
   *
   * @param number   the bank account number
   * @param username
   * @return an object of BankAccount.
   */

  public BankAccount findByNumber(String number, String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("number", number);

    String sql = "SELECT NUMBER, USERNAME FROM BANKACCOUNT WHERE NUMBER = :number AND USERNAME = :username;";

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
        (ResultSet rs, int rowNums) -> {
          BankAccount b1 = new BankAccount();
          b1.setUsername(rs.getString("username"));
          b1.setNumber(rs.getString("number"));
          return b1;
        }
    );

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
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("number", number);

    String sql = "DELETE FROM BANKACCOUNT WHERE USERNAME = :username AND NUMBER = :number;";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }


}

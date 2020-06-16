package com.example.digitalportfolio.repository;

import com.example.digitalportfolio.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository
{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private RowMapper<TransactionDto>  rowMapper = (ResultSet rs, int rowNums) -> {
    TransactionDto b = new TransactionDto();
    b.setSender(rs.getString("sender"));
    b.setReceiver(rs.getString("receiver"));
    b.setMoney(rs.getBigDecimal("money"));
    b.setLocalDateTime(rs.getTimestamp("date"));
    return b;
  };

  @Autowired
  public TransactionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * That's a method wherewith information about transaction is stored into sql table
   * using namedParameterJdbcTemplate.
   *
   * @param sender        the user which sends the money
   * @param receiver      the user which receive the money
   * @param money         the value of money
   * @param localDateTime the time of transaction
   */

  public void save(String sender, String receiver, BigDecimal money, Timestamp localDateTime)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("sender", sender);
    mapSqlParameterSource.addValue("receiver", receiver);
    mapSqlParameterSource.addValue("money", money);
    mapSqlParameterSource.addValue("date", localDateTime);

    String sql = "INSERT INTO TRANSACTION(SENDER, RECEIVER,MONEY,DATE) VALUES (:sender,:receiver,:money,:date) ";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * Retrieves an information about user's transactions, if it's empty an exception is thrown.
   *
   * @param count    the count of transactions which have to be displayed
   * @param username
   * @return List<TransactionDto>
   */

  public List<TransactionDto> findByUserName(int count, String username)
  {
    List<TransactionDto> t = new ArrayList<>();
    try {
      MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
      mapSqlParameterSource.addValue("username", username);
      mapSqlParameterSource.addValue("count", count);
      String sql = "SELECT SENDER , RECEIVER , MONEY , DATE  FROM  TRANSACTION WHERE " +
          "SENDER = :username OR RECEIVER = :username ORDER BY  DATE DESC LIMIT :count;";

      return (namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper));
    }
    catch (EmptyResultDataAccessException e) {
    }
    return t;
  }

  /**
   * Retrieves an information about user's transactions.
   *
   * @param sender   the sender of the money
   * @param receiver the receiver of the money
   * @param money    the value of money
   * @return List<TransactionDto>
   */

  public List<TransactionDto> findBySenderAndReceiverAndMoney(String sender, String receiver, BigDecimal money)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("sender", sender);
    mapSqlParameterSource.addValue("receiver", receiver);
    mapSqlParameterSource.addValue("money", money);
    String sql = "SELECT SENDER , RECEIVER , MONEY , DATE  FROM  TRANSACTION WHERE SENDER = :sender AND RECEIVER = :receiver  AND MONEY = :money ;";

    return (namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, rowMapper));
  }

  /**
   * A method wherewith a transaction can be deleted.
   *
   * @param sender   the sender of the money
   * @param receiver the receiver of the money
   */

  // it`s for test
  public void delete(String sender, String receiver)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("sender", sender);
    mapSqlParameterSource.addValue("receiver", receiver);
    String sql = "DELETE FROM TRANSACTION WHERE SENDER = :sender AND  RECEIVER = :receiver;";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }


}

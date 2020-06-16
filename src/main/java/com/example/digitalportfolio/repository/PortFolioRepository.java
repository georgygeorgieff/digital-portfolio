package com.example.digitalportfolio.repository;

import com.example.digitalportfolio.model.PortFolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;


@Repository
public class PortFolioRepository
{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public PortFolioRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * That's a method wherewith information about portfolio is stored into sql table
   * using namedParameterJdbcTemplate.
   *
   * @param username
   */

  public void save(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("money", BigDecimal.valueOf(0.00));
    String sql = "INSERT INTO PORTFOLIO (MONEY,USERNAME) VALUES (:money,:username)";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * Retrieves an information about user's portfolio
   *
   * @param username
   * @return an object of PortFolio.
   */

  public PortFolio findByUserName(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    String sql = "SELECT MONEY, USERNAME FROM PORTFOLIO WHERE USERNAME = :username";

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, (ResultSet rs, int rowNum) -> {
      PortFolio p1 = new PortFolio();
      p1.setMoney(rs.getBigDecimal("money"));
      p1.setUsername(rs.getString("username"));
      return p1;
    });
  }

  /**
   * Retrieves an information about user's portfolio. Used when need a lock.
   *
   * @param username
   * @return an object of PortFolio.
   */

  public PortFolio findByUserNameForUpdate(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    String sql = "SELECT MONEY, USERNAME FROM PORTFOLIO WHERE USERNAME = :username FOR UPDATE";

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, (ResultSet rs, int rowNum) -> {
      PortFolio p1 = new PortFolio();
      p1.setMoney(rs.getBigDecimal("money"));
      p1.setUsername(rs.getString("username"));
      return p1;
    });
  }

  /**
   * That's a method wherewith information about user's portfolio have to be updated.
   *
   * @param username
   * @param money    the value of money which need to add in portfolio.
   */

  public void update(String username, BigDecimal money)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("money", money);
    String sql = "UPDATE PORTFOLIO SET MONEY = :money WHERE USERNAME = :username";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * That's a method wherewith information about user's portfolio can be deleted.
   *
   * @param username
   */

  public void delete(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    String sql = "DELETE FROM  PORTFOLIO WHERE USERNAME = :username";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }
}

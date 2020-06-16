package com.example.digitalportfolio.repository;

import com.example.digitalportfolio.model.Token;
import com.example.digitalportfolio.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Set;

@Repository
public class TokenRepository
{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public TokenRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public void createToken(String token, Timestamp timestamp, String email)
  {
    String sql = "INSERT INTO TOKEN(token,date,email) VALUES (:token ,:date,:email);";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("token", token);
    mapSqlParameterSource.addValue("date", timestamp);
    mapSqlParameterSource.addValue("email", email);

    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  public Token findUserTokenByEmail(String email)
  {
    String sql = "SELECT * FROM TOKEN WHERE EMAIL = :email";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("email", email);

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
        (ResultSet rs, int rowNum) -> {
          Token t = new Token();
          t.setToken(rs.getString("token"));
          t.setTimestamp(rs.getTimestamp("date").toLocalDateTime());
          t.setEmail(rs.getString("email"));
          return t;
        }
    );

  }

  public Token findUserTokenByTokenNumber(String token)
  {
    String sql = "SELECT * FROM TOKEN WHERE TOKEN = :token";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("token", token);

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
        (ResultSet rs, int rowNum) -> {
          Token t = new Token();
          t.setToken(rs.getString("token"));
          t.setTimestamp(rs.getTimestamp("date").toLocalDateTime());
          t.setEmail(rs.getString("email"));
          return t;
        }
    );
  }

  public void deleteTokenByEmail(String email)
  {
    String sql = "DELETE FROM TOKEN WHERE EMAIL = :email;";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("email", email);

    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  public void deleteTokenByToken(String token)
  {
    String sql = "DELETE FROM TOKEN WHERE token = :token;";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("token", token);

    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }
}

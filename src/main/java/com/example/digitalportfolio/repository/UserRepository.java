package com.example.digitalportfolio.repository;

import com.example.digitalportfolio.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Set;

@Repository
public class UserRepository
{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private RoleRepository             roleRepository;


  @Autowired
  public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, RoleRepository roleRepository)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.roleRepository = roleRepository;
  }

  /**
   * That's a method wherewith an user is created
   * using namedParameterJdbcTemplate.
   *
   * @param username the username which user can log into database.
   * @param password the necessary password for logging.
   * @param email    an email to receive a notifications.
   */

  public void createUser(String username, String password, String email)
  {
    // by default isLocked is false , and balance in portFolio is 0
    String sql = "INSERT INTO USERS(username,password,email) VALUES (:username ,:password,:email);";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("password", password);
    mapSqlParameterSource.addValue("email", email);

    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * That's a method wherewith an admin is created
   * using namedParameterJdbcTemplate.
   *
   * @param username the username which admin can log into database.
   * @param password the necessary password for logging.
   */

  public void createAdmin(String username, String password, String email)
  {
    String sql = "INSERT INTO USERS(username,password,email) VALUES (:username ,:password,:email);";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    mapSqlParameterSource.addValue("password", password);
    mapSqlParameterSource.addValue("email", email);

    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * That's a method wherewith information about user is stored into sql table
   * using namedParameterJdbcTemplate.
   *
   * @param username the username which user can log into database.
   * @param lock     an information about the status of the user, is it locked or not.
   */

  public void setLock(String username, boolean lock)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("lock", lock);
    mapSqlParameterSource.addValue("username", username);
    String sql = "UPDATE USERS SET isLocked= :lock WHERE  USERNAME= :username;";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }


  public void enableUser(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    String sql = "UPDATE USERS SET isEnabled = 'true' WHERE  USERNAME= :username;";
    namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
  }

  /**
   * Retrieves an information about user.
   *
   * @param username the username which user can log into database.
   * @return an object of User.
   */

  public User findByUserName(String username)
  {
    String sql = "SELECT * FROM USERS WHERE USERNAME = :username";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
        (ResultSet rs, int rowNum) -> {
          User user = new User();
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setLocked(rs.getBoolean("islocked"));
          user.setEnabled(rs.getBoolean("isenabled"));
          user.setEmail(rs.getString("email"));
          Set<String> roles = roleRepository.getAllRoles(user.getUsername());
          user.setRole(roles);
          return user;
        }
    );

  }

  public User findByEmail(String email)
  {
    String sql = "SELECT * FROM USERS WHERE EMAIL = :email";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("email", email);

    return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
        (ResultSet rs, int rowNum) -> {
          User user = new User();
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setLocked(rs.getBoolean("islocked"));
          user.setEnabled(rs.getBoolean("isenabled"));
          user.setEmail(rs.getString("email"));
          Set<String> roles = roleRepository.getAllRoles(user.getUsername());
          user.setRole(roles);
          return user;
        }
    );

  }


  /**
   * Retrieves an information about user.
   *
   * @param username the username which user can log into database.
   * @return List<TransactionDto>
   */

  public User findUserPerRegistration(String username)
  {
    String sql = "SELECT * FROM USERS WHERE USERNAME = :username";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);

    User u = null;

    try {
      u = namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
          (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setLocked(rs.getBoolean("islocked"));
            user.setLocked(rs.getBoolean("isenabled"));
            user.setEmail(rs.getString("email"));
            Set<String> roles = roleRepository.getAllRoles(user.getUsername());
            user.setRole(roles);
            return user;
          }
      );
    }
    catch (EmptyResultDataAccessException e) {
    }
    return u;
  }


  public User findEmailPerRegistration(String email)
  {
    String sql = "SELECT * FROM USERS WHERE EMAIL = :email";
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("email", email);

    User u = null;

    try {
      u = namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource,
          (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setLocked(rs.getBoolean("islocked"));
            user.setLocked(rs.getBoolean("isenabled"));
            user.setEmail(rs.getString("email"));
            Set<String> roles = roleRepository.getAllRoles(user.getUsername());
            user.setRole(roles);
            return user;
          }
      );
    }
    catch (EmptyResultDataAccessException e) {
    }
    return u;
  }


  /**
   * Deletes user.
   *
   * @param username the username which user can log into database.
   */

  public void delete(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", username);
    String sql = "DELETE FROM USERS WHERE USERNAME= :username";
    try {
      namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }
    catch (EmptyResultDataAccessException e) {

    }


  }

}



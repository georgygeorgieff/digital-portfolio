package com.example.digitalportfolio.service;

import com.example.digitalportfolio.repository.RoleRepository;
import com.example.digitalportfolio.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService
{
  private RoleRepository roleRepository;
  private Validator      validator;

  @Autowired
  public RoleService(RoleRepository roleRepository, Validator validator)
  {
    this.roleRepository = roleRepository;
    this.validator = validator;
  }

  public Set<String> getAllRoles(String username)
  {
    return roleRepository.getAllRoles(username);
  }


  /**
   * Adds a role into user's list of roles.
   *
   * @param r        the actual role
   * @param username the username which is necessary for logging.
   */

  public void addRole(String r, String username)
  {
    validator.validateBuyerIsNotAdmin(r);
    roleRepository.addRole(r.toUpperCase(), username);
  }

  public void addAdminRole(String r, String username)
  {
    roleRepository.addRole(r.toUpperCase(), username);
  }

  /**
   * Deletes a role from user's list of roles.
   *
   * @param r        the actual role
   * @param username the username which is necessary for logging.
   */

  public void deleteRole(String r, String username)
  {
    roleRepository.deleteRole(r, username);
  }
}

package com.example.digitalportfolio.controller;

import com.example.digitalportfolio.service.AdminService;
import com.example.digitalportfolio.service.RoleService;
import com.example.digitalportfolio.service.UserService;
import com.example.digitalportfolio.validator.ValidPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole(T(com.example.digitalportfolio.role.Roles).ADMIN_ROLE)")
public class AdminController
{
  private AdminService adminService;


  @Autowired
  public AdminController(AdminService adminService)
  {
    this.adminService = adminService;
  }

  /**
   * That's a method wherewith user can be locked by admin using /api/v1/user/setlock
   *
   * @param username the username which user can log into database.
   * @param lock
   * @return ResponseEntity
   */

  @PutMapping("/setlock")
  public ResponseEntity<Void> setLockUser(@RequestParam String username, @RequestParam boolean lock)
  {

    adminService.lockUser(lock, username);
    return ResponseEntity.ok().build();
  }

  /**
   * That's a method wherewith admin can be created using /api/v1/user/createAdmin
   *
   * @param username the username which user can log into database.
   * @param password
   * @return ResponseEntity
   */

  @PostMapping("/createadmin")
  public ResponseEntity<Void> createAdmin(@RequestParam String username, @RequestParam @ValidPassword String password, @RequestParam String email)
  {
    adminService.createAdmin(username, password, email);
    return ResponseEntity.ok().build();
  }

  /**
   * That's a method wherewith admin can be deleted by another admin using /api/v1/user/deleteadmin
   *
   * @param username the username which user can log into database.
   * @return ResponseEntity
   */

  @DeleteMapping("/deleteadmin")
  public ResponseEntity<Void> deleteAdmin(@RequestParam String username)
  {

    adminService.deleteAdmin(username);
    return ResponseEntity.ok().build();
  }

}

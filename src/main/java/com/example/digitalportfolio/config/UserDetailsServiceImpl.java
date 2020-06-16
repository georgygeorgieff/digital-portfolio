package com.example.digitalportfolio.config;

import com.example.digitalportfolio.exception.AccessForbiddenException;
import com.example.digitalportfolio.model.User;
import com.example.digitalportfolio.repository.UserRepository;
import com.example.digitalportfolio.service.AdminService;
import com.example.digitalportfolio.service.RoleService;
import com.example.digitalportfolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService
{


  private UserService    userService;
  private RoleService    roleService;
  private AdminService   adminService;
  private UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserService userService, RoleService roleService,
                                AdminService adminService, UserRepository userRepository)
  {
    this.userService = userService;
    this.roleService = roleService;
    this.adminService = adminService;
    this.userRepository = userRepository;
  }

  public UserDetails loadUserByUsername(String username) throws AccessForbiddenException
  {

    User user1 = userRepository.findUserPerRegistration("admin");
    if (user1 == null) {
      adminService.createAdmin("admin", new BCryptPasswordEncoder().encode("P@rola_1234"), "Admin@admin.ru");
      roleService.addAdminRole("ADMIN", "admin");
    }

    User user = userService.findByUserName(username);

    if (user.isLocked() || !user.isEnabled()) {
      throw new AccessForbiddenException("Access forbidden!");
    }

    Set<String> roles = roleService.getAllRoles(username);
    String[] rolez = roles.toArray(new String[roles.size()]);

    org.springframework.security.core.userdetails.User.UserBuilder builder =
        org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(rolez);

    return builder.build();
  }

}

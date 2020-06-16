package com.example.digitalportfolio.model;


import com.example.digitalportfolio.validator.ValidPassword;

import java.util.Set;

public class User
{

  private String      username;
  @ValidPassword
  private String      password;
  private Set<String> role;
  private boolean     isLocked; // default false
  private boolean     isEnabled; // default false
  private String      email;

  public String getUsername()
  {
    return username;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }


  public boolean isLocked()
  {
    return isLocked;
  }

  public void setLocked(boolean locked)
  {
    isLocked = locked;
  }

  public Set<String> getRole()
  {
    return role;
  }

  public void setRole(Set<String> role)
  {
    this.role = role;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public boolean isEnabled()
  {
    return isEnabled;
  }

  public void setEnabled(boolean enabled)
  {
    isEnabled = enabled;
  }
}

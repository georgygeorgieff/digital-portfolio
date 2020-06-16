package com.example.digitalportfolio.service;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Test
public class UserServiceTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;
  @Autowired
  private UserService      userService;
  @Autowired
  private PortFolioService portFolioService;
  @Autowired
  private RoleService      roleService;
  @Autowired
  private TokenService     tokenService;
  @Autowired
  private AdminService     adminService;


  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @BeforeMethod
  private void createData()
  {

    userService.createUser("ivomir2", new BCryptPasswordEncoder().encode("P@Rola123"), "consumer", "nqmam");
    roleService.addRole("CONSUMER", "ivomir2");
    portFolioService.create("ivomir2");
    userService.enableUser("ivomir2");

    userService.createUser("pesho2", new BCryptPasswordEncoder().encode("P@Rola123"), "merchant", "noM4ail4");
    roleService.addRole("merchant", "pesho2");
    portFolioService.create("pesho2");
    userService.enableUser("pesho2");

    adminService.createAdmin("admin11", new BCryptPasswordEncoder().encode("P@Rola123"), "noM4ail41");


  }

  @AfterMethod
  private void deleteData()
  {
    roleService.deleteRole("CONSUMER", "ivomir2");
    portFolioService.delete("ivomir2");
    tokenService.deleteByEmail("nqmam");
    userService.delete("ivomir2");

    roleService.deleteRole("MERCHANT", "pesho2");
    portFolioService.delete("pesho2");
    tokenService.deleteByEmail("noM4ail4");
    userService.delete("pesho2");

    roleService.deleteRole("admin", "admin11");
    userService.delete("admin11");

  }


  @Test
  public void test_create_user_correct()
  {
    RestAssured.
        given()
        .param("username", "testUser")
        .param("password", "P@Rola123")
        .param("role", "CONSUMER")
        .param("email", "nenene@abv.bg")
        .when().post("/api/v1/user").
        then()
        .assertThat().
        statusCode(200);

    roleService.deleteRole("CONSUMER", "testUser");
    portFolioService.delete("testUser");
    tokenService.deleteByEmail("nenene@abv.bg");
    userService.delete("testUser");
  }

  @Test
  public void test_create_user_incorrect_password()
  {
    RestAssured.
        given()
        .param("username", "ivomirtest")
        .param("password", "PaRola123")//special symbol is necessary
        .param("role", "consumer")
        //.param("email","nqmambe")
        .when().post("/api/v1/user")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_create_user_incorrect() // try to make a user with role admin
  {
    RestAssured.
        given()
        .param("username", "ivomirtest")
        .param("password", "P@Rola123")
        .param("role", "ADMIN")
        //.param("email","nqmambe")
        .when().post("/api/v1/user")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void get_balance_correct()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .when()
        .get("/api/v1/user/balance")
        .then()
        .assertThat()
        .statusCode(200);
  }


  @Test
  public void get_balance_incorrect() // try with admin to get balance
  {
    RestAssured
        .given().auth().basic("admin11", "P@Rola123")
        .when()
        .get("/api/v1/user/balance")
        .then()
        .assertThat()
        .statusCode(403);
  }

}

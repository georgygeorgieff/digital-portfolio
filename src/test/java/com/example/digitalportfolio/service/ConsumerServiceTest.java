package com.example.digitalportfolio.service;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Test
public class ConsumerServiceTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;
  @Autowired
  private UserService        userService;
  @Autowired
  private PortFolioService   portFolioService;
  @Autowired
  private RoleService        roleService;
  @Autowired
  private BankCardService    bankCardService;
  @Autowired
  private TransactionService transactionService;
  @Autowired
  private MailService        mailService;
  @Autowired
  private TokenService       tokenService;

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }


  @BeforeMethod
  private void createData()
  {
    userService.createUser("ivomir2", new BCryptPasswordEncoder().encode("P@Rola123"), "consumer", "nqmam@abv.bg");
    roleService.addRole("CONSUMER", "ivomir2");
    portFolioService.create("ivomir2");
    userService.enableUser("ivomir2");
    portFolioService.update("ivomir2", BigDecimal.valueOf(1000));

    userService.createUser("halil", new BCryptPasswordEncoder().encode("P@Rola123"), "consumer", "vasilev6996999@gmail.com");
    roleService.addRole("CONSUMER", "halil");
    portFolioService.create("halil");
    userService.enableUser("halil");
    portFolioService.update("halil", BigDecimal.valueOf(1000));

    userService.createUser("pesho2", new BCryptPasswordEncoder().encode("P@Rola123"), "merchant", "noM4ail4@gmail.com");
    roleService.addRole("merchant", "pesho2");
    portFolioService.create("pesho2");
    userService.enableUser("pesho2");
    portFolioService.update("pesho2", BigDecimal.valueOf(1000));

  }

  @AfterMethod
  private void deleteData()
  {
    roleService.deleteRole("CONSUMER", "ivomir2");
    portFolioService.delete("ivomir2");
    tokenService.deleteByEmail("nqmam@abv.bg");
    userService.delete("ivomir2");

    roleService.deleteRole("CONSUMER", "halil");
    portFolioService.delete("halil");
    tokenService.deleteByEmail("vasilev6996999@gmail.com");
    userService.delete("halil");

    roleService.deleteRole("MERCHANT", "pesho2");
    portFolioService.delete("pesho2");
    tokenService.deleteByEmail("noM4ail4@gmail.com");
    userService.delete("pesho2");
  }

  @Test
  public void test_transfer_correct()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("username", "halil")
        .param("money", "20")
        .when()
        .post("/api/v1/user/transfer")
        .then()
        .assertThat()
        .statusCode(200);

    transactionService.delete("ivomir2", "halil");
  }

  @Test
  public void test_transfer_incorrect_receiver_not_exist()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("username", "AAAAAAAA")
        .param("money", "20")
        .when()
        .post("/api/v1/user/transfer")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void test_transfer_incorrect_not_enough_money()
  {

    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("username", "halil")
        .param("money", "10500")
        .when()
        .post("/api/v1/user/transfer")
        .then()
        .assertThat()
        .statusCode(400);

  }

  @Test
  public void test_transfer_incorrect_receiver_notConsumer()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("username", "pesho2")
        .param("money", "9")
        .when()
        .post("/api/v1/user/transfer")
        .then()
        .assertThat()
        .statusCode(400); //its not consumer => bad request
  }

  @Test
  public void test_transfer_incorrect_transferMoney_negative()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("username", "halil")
        .param("money", "-20")
        .when()
        .post("/api/v1/user/transfer")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_add_bankCard_correct()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("number", "P@Rola123789")
        .when()
        .post("/api/v1/user/card")
        .then()
        .assertThat()
        .statusCode(200);

    bankCardService.delete("ivomir2", "P@Rola123789");
  }

  @Test
  public void test_add_bankCard_incorrect_user_notExist()
  {
    RestAssured
        .given().auth().basic("azzzzz", "P@Rola123")
        .param("number", "P@Rola123789")
        .when()
        .post("/api/v1/user/card")
        .then()
        .assertThat()
        .statusCode(401);
  }

  @Test
  public void test_add_bankCard_incorrect_user_merchant()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("number", "P@Rola123789")
        .when()
        .post("/api/v1/user/card")
        .then()
        .assertThat()
        .statusCode(403);
  }
}

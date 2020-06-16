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
import java.sql.Timestamp;
import java.time.LocalDateTime;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Test
public class MerchantServiceTest extends AbstractTestNGSpringContextTests
{
  @LocalServerPort
  int port;
  @Autowired
  private UserService        userService;
  @Autowired
  private PortFolioService   portFolioService;
  @Autowired
  private BankAccountService bankAccountService;
  @Autowired
  private TransactionService transactionService;
  @Autowired
  private RoleService        roleService;
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

    userService.createUser("pesho2", new BCryptPasswordEncoder().encode("P@Rola123"), "merchant", "noMail@abv.bg");
    roleService.addRole("merchant", "pesho2");
    portFolioService.create("pesho2");
    userService.enableUser("pesho2");
    portFolioService.update("pesho2", BigDecimal.valueOf(1000));

    userService.createUser("spiro2", new BCryptPasswordEncoder().encode("P@Rola123"), "merchant", "noMail2@abv.bg");
    roleService.addRole("merchant", "spiro2");
    portFolioService.create("spiro2");
    userService.enableUser("spiro2");
  }

  @AfterMethod
  private void deleteData()
  {
    roleService.deleteRole("CONSUMER", "ivomir2");
    portFolioService.delete("ivomir2");
    tokenService.deleteByEmail("nqmam@abv.bg");
    userService.delete("ivomir2");

    roleService.deleteRole("MERCHANT", "pesho2");
    portFolioService.delete("pesho2");
    tokenService.deleteByEmail("noMail@abv.bg");
    userService.delete("pesho2");

    roleService.deleteRole("MERCHANT", "spiro2");
    portFolioService.delete("spiro2");
    tokenService.deleteByEmail("noMail2@abv.bg");
    userService.delete("spiro2");

  }

  @Test
  public void test_add_bankAccount_correct()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("number", "bg123rtt11")
        .when()
        .post("/api/v1/user/account")
        .then()
        .assertThat()
        .statusCode(200);

    bankAccountService.delete("pesho2", "bg123rtt11");
  }

  @Test
  public void test_add_bankAccount_incorrect_user_consumer()
  {
    RestAssured
        .given().auth().basic("ivomir2", "P@Rola123")
        .param("number", "bg123rtt11")
        .when()
        .post("/api/v1/user/account")
        .then()
        .assertThat()
        .statusCode(403);
  }

  @Test
  public void test_get_money_for_purchase_correct()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "20")
        .param("username", "ivomir2")
        .when()
        .post("/api/v1/user/getmoney")
        .then()
        .assertThat()
        .statusCode(200);

    transactionService.delete("ivomir2", "pesho2");
  }

  @Test
  public void test_get_money_for_purchase_incorrect_consumer()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "20")
        .param("username", "incorrect_username")
        .when()
        .post("/api/v1/user/getmoney")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void test_get_money_for_purchase_incorrect_notEnoughMoney()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "2000")
        .param("username", "ivomir2")
        .when()
        .post("/api/v1/user/getmoney")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_get_money_for_purchase_incorrect_NegativeMoney()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "-20")
        .param("username", "ivomir2")
        .when()
        .post("/api/v1/user/getmoney")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_get_money_for_purchase_incorrect_userIsNotConsumer()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "50")
        .param("username", "spiro2")
        .when()
        .post("/api/v1/user/getmoney")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  public void test_back_money_correct()
  {
    transactionService.save("ivomir2", "pesho2",
        BigDecimal.valueOf(50), Timestamp.valueOf(LocalDateTime.now()));

    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "50")
        .param("username", "ivomir2")
        .when()
        .post("/api/v1/user/backmoney")
        .then()
        .assertThat()
        .statusCode(200);

    transactionService.delete("pesho2", "ivomir2");
    transactionService.delete("ivomir2", "pesho2");

  }

  @Test
  public void test_back_money_incorrect_userIsDifferent()
  {
    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "50")
        .param("username", "kirchooo")
        .when()
        .post("/api/v1/user/backmoney")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void test_back_money_incorrect_money_is_different()
  {
    transactionService.save("ivomir2", "pesho2",
        BigDecimal.valueOf(50), Timestamp.valueOf(LocalDateTime.now()));

    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "35")
        .param("username", "ivomir2")
        .when()
        .post("/api/v1/user/backmoney")
        .then()
        .assertThat()
        .statusCode(400);

    transactionService.delete("ivomir2", "pesho2");
  }

  @Test
  public void test_transfer_money_to_bank_account_correct()
  {
    bankAccountService.save("bg123rtt11", "pesho2");

    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "35")
        .param("number", "bg123rtt11")
        .when()
        .post("/api/v1/user/tobankaccount")
        .then()
        .assertThat()
        .statusCode(200);

    bankAccountService.delete("pesho2", "bg123rtt11");
    transactionService.delete("pesho2", "pesho2");
  }

  @Test
  public void test_transfer_money_to_bank_account_incorrect_bank_account()
  { //our user does not have account for now

    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "35")
        .param("number", "bg123rtt11")
        .when()
        .post("/api/v1/user/tobankaccount")
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  public void test_transfer_money_to_bank_account_incorrect_money_isNotEnough()
  {
    bankAccountService.save("bg123rtt11", "pesho2");

    RestAssured
        .given().auth().basic("pesho2", "P@Rola123")
        .param("money", "1065")
        .param("number", "bg123rtt11")
        .when()
        .post("/api/v1/user/tobankaccount")
        .then()
        .assertThat()
        .statusCode(400);

    bankAccountService.delete("pesho2", "bg123rtt11");
    transactionService.delete("pesho2", "pesho2");
  }


}

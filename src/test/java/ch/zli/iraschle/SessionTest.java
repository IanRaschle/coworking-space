package ch.zli.iraschle;

import ch.zli.iraschle.model.session.Credentials;
import ch.zli.iraschle.model.user.ApplicationUserEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class SessionTest {

    @Inject
    TestDataService testDataService;

    @AfterEach
    void resetDB() {
        testDataService.loadTestData(null);
    }

    @Test
    void testSignUp() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setEmail("test@tttt.ch");
        applicationUser.setFirstname("test");
        applicationUser.setLastname("t");
        applicationUser.setPassword("pwd");
        given().header("Content-type", "application/json").body(applicationUser).when().post("/sign-up").then()
                .statusCode(200);
    }

    @Test
    void testSignUpInvalidData() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setEmail("test@t.ch");
        applicationUser.setFirstname("test");
        given().header("Content-type", "application/json").body(applicationUser).when().post("/sign-up").then()
                .statusCode(400);
    }

    @Test
    void testSignUpExistingEmail() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setEmail("hans.bra@gmail.com");
        applicationUser.setFirstname("test");
        applicationUser.setLastname("t");
        applicationUser.setPassword("pwd");
        given().header("Content-type", "application/json").body(applicationUser).when().post("/sign-up").then()
                .statusCode(409);
    }

    @Test
    void testSignIn() {
        Credentials credentials = new Credentials();
        credentials.setEmail("hans.bra@gmail.com");
        credentials.setPassword("admin");
        given().header("Content-type", "application/json").body(credentials).when().post("/sign-in").then()
                .statusCode(200);
    }

    @Test
    void testSignInWithWrongCredentials() {
        Credentials credentials = new Credentials();
        credentials.setEmail("hans.bar@gmail.com");
        credentials.setPassword("adm");
        given().header("Content-type", "application/json").body(credentials).when().post("/sign-in").then()
                .statusCode(401);
    }
}

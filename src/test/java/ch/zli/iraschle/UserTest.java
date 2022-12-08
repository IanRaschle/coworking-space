package ch.zli.iraschle;

import ch.zli.iraschle.model.user.ApplicationUserEntity;
import ch.zli.iraschle.util.ApplicationUserDto;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class UserTest {

    @Inject
    TestDataService testDataService;

    @AfterEach
    void resetDB() {
        testDataService.loadTestData(null);
    }

    @Test
    void testChangeEmailWithoutRights() {
        given().header("Content-type", "text/plain").body("t.t@q.com").when().patch("/users/change/email")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testChangeEmailWithoutEmail() {
        given().header("Content-type", "text/plain").when().patch("/users/change/email")
                .then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testChangeEmail() {
        given().header("Content-type", "text/plain").body("ruedi@gmail.com").when().patch("/users/change/email")
                .then().statusCode(200);
    }

    @Test
    void testChangePasswordWithoutRights() {
        given().header("Content-type", "text/plain").body("test").when().patch("/users/change/password")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testChangePasswordWithoutPassword() {
        given().header("Content-type", "text/plain").when().patch("/users/change/password")
                .then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testChangePassword() {
        given().header("Content-type", "text/plain").body("test").when().patch("/users/change/password")
                .then().statusCode(200);
    }

    @Test
    void testCreateUserWithoutLogin() {
        ApplicationUserDto applicationUserDto = new ApplicationUserDto();
        applicationUserDto.setEmail("test.p");
        applicationUserDto.setFirstname("löu");
        applicationUserDto.setLastname("rötele");
        applicationUserDto.setPassword("cs");
        given().header("Content-type", "application/json").body(applicationUserDto).when().post("/users").then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testCreateUserWithoutAdminLogin() {
        ApplicationUserDto applicationUserDto = new ApplicationUserDto();
        applicationUserDto.setEmail("test.p");
        applicationUserDto.setFirstname("löu");
        applicationUserDto.setLastname("rötele");
        applicationUserDto.setPassword("cs");
        given().header("Content-type", "application/json").body(applicationUserDto).when().post("/users").then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testCreateUserWithInvalidData() {
        ApplicationUserDto applicationUserDto = new ApplicationUserDto();
        applicationUserDto.setEmail("test.p");
        applicationUserDto.setPassword("cs");
        given().header("Content-type", "application/json").body(applicationUserDto).when().post("/users").then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testCreateUser() {
        ApplicationUserDto applicationUserDto = new ApplicationUserDto();
        applicationUserDto.setEmail("test.p");
        applicationUserDto.setFirstname("löu");
        applicationUserDto.setLastname("rötele");
        applicationUserDto.setPassword("cs");
        given().header("Content-type", "application/json").body(applicationUserDto).when().post("/users").then()
                .statusCode(201);
    }

    @Test
    void getUsersWithoutLogin() {
        given().when().get("/users").then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void getUsersWithoutAminRights() {
        given().when().get("/users").then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void getUsers() {
        given().when().get("/users").then()
                .statusCode(200)
                .and().body("size()", is(2));
    }

    @Test
    void deleteUserWithoutLogin() {
        given().when().delete("/users/1").then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void deleteUserWithoutAminRights() {
        given().when().delete("/users/1").then()
                .statusCode(403);
    }

    /**
     * This test has to be executed lonely. It will fail if you start the whole test due to the inability of H2 to reset the identity
     */
    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void deleteUser() {
        given().when().delete("/users/1").then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void deleteNotExistingUser() {
        given().when().delete("/users/200").then()
                .statusCode(400);
    }

    @Test
    void updateUserWithoutLogin() {
        given().header("Content-type", "application/json").when().put("/users").then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void updateUserWithoutAminRights() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setId(1l);
        applicationUser.setEmail("test.p");
        applicationUser.setFirstname("löu");
        applicationUser.setLastname("rötele");
        applicationUser.setPassword("cs");
        given().header("Content-type", "application/json").body(applicationUser).when().put("/users").then()
                .statusCode(403);
    }

    /**
     * This test has to be executed lonely. It will fail if you start the whole test due to the inability of H2 to reset the identity
     */
    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testUpdateUser() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setId(1l);
        applicationUser.setEmail("test.p");
        applicationUser.setFirstname("löu");
        applicationUser.setLastname("rötele");
        applicationUser.setPassword("cs");
        given().header("Content-type", "application/json").body(applicationUser).when().put("/users").then()
                .statusCode(200);
    }


//    @Test
//    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
//    @JwtSecurity(claims = {
//            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
//    })
//    void testGetBookingsSortedWithoutAdminRights() {
//        given().when().get("/bookings/sorted")
//                .then().statusCode(403);
//    }
//
//    @Test
//    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
//    @JwtSecurity(claims = {
//            @Claim(key = "upn", value = "hans.bar@gmail.com")
//    })
//    void testGetBookingsSortedWithAdminRights() {
//        given().when().get("/bookings/sorted").then()
//                .statusCode(200).and().body("size()", is(4));
//    }

}

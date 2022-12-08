package ch.zli.iraschle;

import ch.zli.iraschle.model.booking.BookingDuration;
import ch.zli.iraschle.model.booking.BookingEntity;
import ch.zli.iraschle.model.booking.BookingState;
import ch.zli.iraschle.model.user.ApplicationUserEntity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BookingTest {

    @Inject
    TestDataService testDataService;

    @AfterEach
    void resetDB() {
        testDataService.loadTestData(null);
    }

    @Test
    void testGetBookingsSortedWithoutRights() {
        given().when().get("/bookings/sorted")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testGetBookingsSortedWithoutAdminRights() {
        given().when().get("/bookings/sorted")
                .then().statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testGetBookingsSortedWithAdminRights() {
        testDataService.loadTestDataWithRelativeDate();
        given().when().get("/bookings/sorted").then()
                .statusCode(200).and().body("size()", is(2));
    }

    @Test
    void testGetBookingsWithoutRights() {
        given().when().get("/bookings")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testGetBookingsWithoutAdminRights() {
        given().when().get("/bookings")
                .then().statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testGetBookingsWithAdminRights() {
        given().when().get("/bookings").then()
                .statusCode(200).and().body("size()", is(4));
    }

    @Test
    void testGetBookingWithoutRights() {
        given().when().get("/bookings/1")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testGetBookingWithoutAdminRights() {
        given().when().get("/bookings/1")
                .then().statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testGetBookingWithAdminRights() {
        given().when().get("/bookings/1").then()
                .statusCode(200)
                .and().body(containsString("FULLDAY"))
                .and().body(containsString("hans.bra@gmail.com"));
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testGetNotExistingBooking() {
        given().when().get("/bookings/200").then()
                .statusCode(400)
                .and().body(containsString("There is no Booking with this id"));
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testDeleteYourBooking() {
        given().when().delete("/bookings/3").then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testDeleteNotYourBooking() {
        given().when().delete("/bookings/1").then()
                .statusCode(403)
                .and().body(containsString("You are only allowed to manipulate your own bookings"));
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testDeleteBookingWithAdminRight() {
        given().when().delete("/bookings/3").then()
                .statusCode(204);
    }

    @Test
    void testCreateWithoutRights() {
        given().header("Content-type", "application/json").when().post("/bookings")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testCreateBookingWithAdminRightsWithoutBody() {
        given().header("Content-type", "application/json").when().post("/bookings").then()
                .statusCode(400)
                .and().body(is("The submitted booking is not valid or null"));
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bra@gmail.com")
    })
    void testCreateBookingWithAdminRights() {
        BookingEntity booking = new BookingEntity();
        booking.setDate(LocalDate.of(2022, 12, 5));
        booking.setState(BookingState.PENDING);
        booking.setDuration(BookingDuration.FULLDAY);
        given().header("Content-type", "application/json")
                .body(booking).when().post("/bookings").then()
                .statusCode(201)
                .and().body(containsString("Hans-ueli"));
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testCreateBookingWithoutAdminRightsWithBodyWithYourEmail() {
        BookingEntity booking = new BookingEntity();
        booking.setDate(LocalDate.of(2022, 12, 10));
        booking.setState(BookingState.PENDING);
        booking.setDuration(BookingDuration.MORNING);
        ApplicationUserEntity testMember = testDataService.getTestMember();
        testMember.setId(2l);
        booking.setApplicationUser(testMember);
        given().header("Content-type", "application/json")
                .body(booking).when().post("/bookings").then()
                .statusCode(201)
                .and().body(containsString("jonas.lukas@gmail.com"));
    }

    @Test
    void testUpdateWithoutRights() {
        given().when().put("/bookings")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testUpdateBookingWithoutAdminRights() {
        given().when().put("/bookings").then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testUpdateBookingWithAdminRightsWithoutBody() {
        given().when().put("/bookings").then()
                .statusCode(400)
                .and().body(is("The submitted booking is not valid or null"));
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testUpdateNotExistingBookingWithAdminRightsWithBody() {
        BookingEntity bookingEntity = given().when().get("/bookings/1").getBody().as(BookingEntity.class);
        Assertions.assertEquals(BookingDuration.FULLDAY, bookingEntity.getDuration());

        bookingEntity.setId(200);
        given().header("Content-type", "application/json")
                .body(bookingEntity).when().put("/bookings").then()
                .statusCode(400)
                .and().body(is("There is no Booking with this id"));
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testUpdateBookingWithAdminRightsWithBody() {
        BookingEntity bookingEntity = given().when().get("/bookings/1").getBody().as(BookingEntity.class);
        Assertions.assertEquals(BookingDuration.FULLDAY, bookingEntity.getDuration());

        bookingEntity.setDuration(BookingDuration.AFTERNOON);
        given().header("Content-type", "application/json")
                .body(bookingEntity).when().put("/bookings").then()
                .statusCode(200)
                .and().body(containsString("AFTERNOON"));
    }

    @Test
    void testGetBookingStateWithoutRights() {
        given().when().get("/bookings/state/1")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testGetNotYourBookingState() {
        given().when().get("/bookings/state/1")
                .then().statusCode(403)
                .and().body(is("You are only allowed to manipulate your own bookings"));
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testGetBookingStateWithAdminRights() {
        given().when().get("/bookings/state/3").then()
                .statusCode(200).and().body(is("ACCEPTED"));
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testGetYourBookingState() {
        given().when().get("/bookings/state/3")
                .then().statusCode(200)
                .and().body(is("ACCEPTED"));
    }

    @Test
    void testAcceptBookingWithoutRights() {
        given().when().post("/bookings/accept/4")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testAcceptBookingWithoutAdminRights() {
        given().when().post("/bookings/accept/4")
                .then().statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testAcceptBookingWithAdminRights() {
        given().when().post("/bookings/accept/4").then()
                .statusCode(200);
        given().when().get("/bookings/4").then()
                .statusCode(200)
                .and().body(containsString("ACCEPTED"));
    }

    @Test
    void testDenyBookingWithoutRights() {
        given().when().post("/bookings/deny/3")
                .then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "jonas.lukas@gmail.com", roles = { "MEMBER" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "jonas.lukas@gmail.com")
    })
    void testDenyBookingWithoutAdminRights() {
        given().when().post("/bookings/deny/3")
                .then().statusCode(403);
    }

    @Test
    @TestSecurity(user = "hans.bar@gmail.com", roles = { "ADMINISTRATOR" })
    @JwtSecurity(claims = {
            @Claim(key = "upn", value = "hans.bar@gmail.com")
    })
    void testDenyBookingWithAdminRights() {
        given().when().post("/bookings/deny/3").then()
                .statusCode(200);
        given().when().get("/bookings/4").then()
                .statusCode(200)
                .and().body(containsString("DENIED"));
    }
}

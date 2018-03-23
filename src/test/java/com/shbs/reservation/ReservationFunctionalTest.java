package com.shbs.reservation;

import com.shbs.FunctionalTest;
import com.shbs.api.reservation.ReservationRequest;
import com.shbs.common.reservation.Reservation;
import com.shbs.common.reservation.ReservationRepository;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import static com.shbs.common.Constant.ZONE_OFFSET;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationFunctionalTest extends FunctionalTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testCreate_Success() {
        final ReservationRequest request = new ReservationRequest(1, 1, 2,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        final Map<String, Object> response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .post("/reservations")
                .then()
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .body("id", notNullValue())
                    .body("room_type_id", equalTo(1))
                    .body("customer_id", equalTo(1))
                    .body("quantity", equalTo(2))
                    .body("start_date", equalTo("2100-01-01"))
                    .body("end_date", equalTo("2100-01-04"))
                    .body("cancelled", equalTo(Boolean.FALSE))
                    .body("created_at", notNullValue())
                    .body("updated_at", notNullValue())
                    .extract().as(Map.class);

        assertThat(response).containsOnlyKeys(
            "id", "room_type_id", "customer_id", "quantity", "start_date", "end_date", "cancelled", "created_at",
                "updated_at");

        final Optional<Reservation> newReservation = reservationRepository
                .findOne(Integer.valueOf(response.get("id").toString()));

        assertThat(newReservation.isPresent());
    }

    @Test
    public void testCreate_StartDateAfterEndDate() {
        final ReservationRequest request = new ReservationRequest(1, 1, 2,
                ZonedDateTime.of(LocalDate.parse("2101-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .post("/reservations")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("start_date_after_end_date"))
                    .body("error_description", equalTo("Start date must be before end date"));
    }

    @Test
    public void testCreate_DateSpecifiedAlreadyPassed() {
        final ReservationRequest request = new ReservationRequest(1, 1, 2,
                ZonedDateTime.of(LocalDate.parse("2001-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .post("/reservations")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("date_specified_already_passed"))
                    .body("error_description", equalTo("Start date and end date must be in the future"));
    }

    @Test
    public void testCreate_AvailableRoomsNotEnough() {
        final ReservationRequest request = new ReservationRequest(1, 1, 2000,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .post("/reservations")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("available_rooms_not_enough"))
                    .body("error_description", equalTo("Quantity requested is bigger than the " +
                            "available rooms for the specified type "));
    }

    @Test
    public void testFind_Success() {
        final Map<String, Object> response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .get("/reservations/1")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(1))
                    .body("room_type_id", equalTo(1))
                    .body("customer_id", equalTo(1))
                    .body("quantity", equalTo(5))
                    .body("start_date", equalTo("2019-03-21"))
                    .body("end_date", equalTo("2019-04-21"))
                    .body("cancelled", equalTo(Boolean.FALSE))
                    .body("created_at", notNullValue())
                    .body("updated_at", notNullValue())
                    .extract().as(Map.class);

        assertThat(response).containsOnlyKeys(
                "id", "room_type_id", "customer_id", "quantity", "start_date", "end_date", "cancelled", "created_at",
                "updated_at");
    }

    @Test
    public void testFind_NotFound() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .get("/reservations/99")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("not_found"))
                    .body("error_description", equalTo("Reservation with id 99 is not found or it has " +
                            "been cancelled"));
    }

    @Test
    public void testUpdate_Success() {
        final ReservationRequest request = new ReservationRequest(1, 1, 10,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        final Map<String, Object> response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/1")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(1))
                    .body("room_type_id", equalTo(1))
                    .body("customer_id", equalTo(1))
                    .body("quantity", equalTo(10))
                    .body("start_date", equalTo("2100-01-01"))
                    .body("end_date", equalTo("2100-01-04"))
                    .body("cancelled", equalTo(Boolean.FALSE))
                    .body("created_at", notNullValue())
                    .body("updated_at", notNullValue())
                    .extract().as(Map.class);

        assertThat(response).containsOnlyKeys(
                "id", "room_type_id", "customer_id", "quantity", "start_date", "end_date", "cancelled", "created_at",
                "updated_at");

        final Optional<Reservation> newReservation = reservationRepository
                .findOne(Integer.valueOf(response.get("id").toString()));

        assertThat(newReservation.get().getQuantity()).isEqualTo(10);
    }

    @Test
    public void testUpdate_ReservationStartDateHasPassed() {
        final Integer reservationId = testHelper.createReservation(1, 1, 8,
                ZonedDateTime.now(), ZonedDateTime.now().plusHours(1), Boolean.FALSE);

        final ReservationRequest request = new ReservationRequest(1, 1, 10,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/" + reservationId)
                .then()
                    .statusCode(400)
                    .body("error", equalTo("reservation_start_date_has_passed"))
                    .body("error_description", equalTo("Reservation Start Date has passed"));
    }

    @Test
    public void testUpdate_StartDateAfterEndDate() {
        final ReservationRequest request = new ReservationRequest(1, 1, 10,
                ZonedDateTime.of(LocalDate.parse("2101-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/1")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("start_date_after_end_date"))
                    .body("error_description", equalTo("Start date must be before end date"));
    }

    @Test
    public void testUpdate_DateSpecifiedAlreadyPassed() {
        final ReservationRequest request = new ReservationRequest(1, 1, 10,
                ZonedDateTime.of(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/1")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("date_specified_already_passed"))
                    .body("error_description", equalTo("Start date and end date must be in the future"));
    }

    @Test
    public void testUpdate_AvailableRoomsNotEnough() {
        final ReservationRequest request = new ReservationRequest(1, 1, 2000,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/1")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("available_rooms_not_enough"))
                    .body("error_description", equalTo("Quantity requested is bigger than the " +
                            "available rooms for the specified type "));
    }

    @Test
    public void testUpdate_NotFound() {
        final ReservationRequest request = new ReservationRequest(1, 1, 10,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/99")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("not_found"))
                    .body("error_description", equalTo("Reservation with id 99 is not found or it has " +
                            "been cancelled"));
    }

    @Test
    public void testUpdate_RoomTypeNotFound() {
        final ReservationRequest request = new ReservationRequest(99, 1, 10,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .patch("/reservations/1")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("not_found"))
                    .body("error_description", equalTo("RoomType with Id 99 is not found"));
    }

    @Test
    public void testCancel_Success() {
        final Integer reservationId = testHelper.createReservation(1, 1, 8,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                Boolean.FALSE);

        final Map<String, Object> response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .patch("/reservations/" + reservationId + "/cancel")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(reservationId))
                    .body("room_type_id", equalTo(1))
                    .body("customer_id", equalTo(1))
                    .body("quantity", equalTo(8))
                    .body("start_date", equalTo("2100-01-01"))
                    .body("end_date", equalTo("2100-01-04"))
                    .body("cancelled", equalTo(Boolean.TRUE))
                    .body("created_at", notNullValue())
                    .body("updated_at", notNullValue())
                    .extract().as(Map.class);

        assertThat(response).containsOnlyKeys(
                "id", "room_type_id", "customer_id", "quantity", "start_date", "end_date", "cancelled", "created_at",
                "updated_at");

        final Optional<Reservation> newReservation = reservationRepository
                .findOne(Integer.valueOf(response.get("id").toString()));

        assertThat(newReservation.get().getCancelled()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void testCancel_NotFound() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .patch("/reservations/99/cancel")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("not_found"))
                    .body("error_description", equalTo("Reservation with id 99 is not found or it has " +
                            "been cancelled"));
    }
}
package com.shbs.reservation;

import com.shbs.FunctionalTest;
import com.shbs.api.reservation.ReservationRequest;
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

import static com.shbs.common.Constant.ZONE_OFFSET;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class RaceConditionFunctionalTest extends FunctionalTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testCreateReservation() throws InterruptedException {
        final Long reservationCount = reservationRepository.count();

        final Integer customerId1 = testHelper.createCustomer("customer1");
        final Integer customerId2 = testHelper.createCustomer("customer2");

        final Thread thread1 = new Thread(() -> createReservation(customerId1));
        final Thread thread2 = new Thread(() -> createReservation(customerId2));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        final Long newReservationCount = reservationRepository.count();

        assertThat(newReservationCount - reservationCount).isEqualTo(1);
    }

    private void createReservation(Integer customerId) {
        final ReservationRequest request = new ReservationRequest(1, customerId, 30,
                ZonedDateTime.of(LocalDate.parse("2100-01-01", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET),
                ZonedDateTime.of(LocalDate.parse("2100-01-04", DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.MIN,
                        ZONE_OFFSET));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                    .post("/reservations");
    }
}
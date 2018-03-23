package com.shbs.roomtype;

import com.shbs.FunctionalTest;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoomTypeFunctionalTest extends FunctionalTest {

    @Test
    public void testFindAvailable_Success() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .get("/room-types/available?start=2020-03-21&end=2020-03-22")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", hasItems(1))
                    .body("type", hasItems("executive"))
                    .body("description", hasItems("executive room only for 1 person"))
                    .body("image", hasItems(""))
                    .body("available_quantity", hasItems(30))
                    .body("price", hasItems(1000f));
    }

    @Test
    public void testFindAvailable_SomeRoomTypesAlreadyReserved() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .get("/room-types/available?start=2019-03-21&end=2019-03-22")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", hasItems(1))
                    .body("type", hasItems("executive"))
                    .body("description", hasItems("executive room only for 1 person"))
                    .body("image", hasItems(""))
                    .body("available_quantity", hasItems(25))
                    .body("price", hasItems(1000f));
    }

    @Test
    public void testFindAvailable_StartDateAfterEndDate() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .get("/room-types/available?start=2019-03-23&end=2019-03-22")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("start_date_after_end_date"))
                    .body("error_description", equalTo("Start date must be before end date"));
    }

    @Test
    public void testFindAvailable_DateSpecifiedAlreadyPassed() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                    .get("/room-types/available?start=2011-03-23&end=2019-03-22")
                .then()
                    .statusCode(400)
                    .body("error", equalTo("date_specified_already_passed"))
                    .body("error_description", equalTo("Start date and end date must be in the future"));
    }
}
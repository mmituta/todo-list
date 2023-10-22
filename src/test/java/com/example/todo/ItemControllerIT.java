package com.example.todo;


import com.example.todo.items.CurrentDateTimeProvider;
import com.example.todo.items.ItemDetailsDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ItemControllerIT {

    @MockBean
    private CurrentDateTimeProvider currentDateTimeProvider;
    private static final String FUTURE_DATE = "2023-10-22T15:35:30Z";
    private static final String NOW = "2023-10-21T12:00:00Z";

    @BeforeEach
    void setUpCurrentDateTime() {
        Mockito.when(this.currentDateTimeProvider.now()).thenReturn(OffsetDateTime.parse(NOW));
    }

    @Test
    void shouldCreateItem() throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("description", "test description");
        requestParams.put("dueDateTime", FUTURE_DATE);

        given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then()
                .statusCode(equalTo(201))
                .body("id", notNullValue())
                .body("description", equalTo("test description"))
                .body("dueDateTime", equalTo(FUTURE_DATE))
                .body("created", equalTo(NOW));
    }

    @Test
    void shouldReturnBadRequestResponseWhenDueDateTimeIsMissingInCreateRequest() {
        String body = newCreateItemBody("description", null);

        given().body(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then()
                .statusCode(equalTo(400));
    }

    @Test
    void shouldReturnBadRequestResponseWhenDescriptionIsMissingInCreateRequest() {
        String body = newCreateItemBody(null, FUTURE_DATE);

        given().body(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then()
                .statusCode(equalTo(400));
    }

    @Test
    void shouldReturnBadRequestResponseWhenDescriptionIsBlankInCreateRequest() {
        String body = newCreateItemBody("  ", FUTURE_DATE);

        given().body(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then()
                .statusCode(equalTo(400));
    }

    @Test
    void shouldCreateAndGetDetailsOfItem() {

        String body = newCreateItemBody("detailed description", FUTURE_DATE);
        String createdId = given().body(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path("id");

        when().get("/items/{id}", createdId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdId))
                .body("description", equalTo("detailed description"))
                .body("dueDateTime", equalTo(FUTURE_DATE))
                .body("created", equalTo(NOW));
    }

    @Test
    void shouldGetAllItems() {

        String firstId = given().body(newCreateItemBody("First", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path("id");


        String secondId = given().body(newCreateItemBody("Second", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path("id");

        ItemDetailsDto[] allItems = when().get("/items").as(ItemDetailsDto[].class);
        assertThat(allItems).contains(detailsDto(firstId, "First", FUTURE_DATE, NOW), detailsDto(secondId, "Second", FUTURE_DATE, NOW));
    }

    private static ItemDetailsDto detailsDto(String id, String description, String dueDateTime, String created) {
        return new ItemDetailsDto(UUID.fromString(id), description, OffsetDateTime.parse(dueDateTime), OffsetDateTime.parse(created));
    }

    private String newCreateItemBody(String description, String dueDateTime) {

        JSONObject requestParams = new JSONObject();
        try {
            if (description != null) {
                requestParams.put("description", description);
            }
            if (dueDateTime != null) {
                requestParams.put("dueDateTime", dueDateTime);
            }
        } catch (JSONException exception) {
            fail("Exception when constructing 'create item' request", exception);
        }
        return requestParams.toString();

    }
}
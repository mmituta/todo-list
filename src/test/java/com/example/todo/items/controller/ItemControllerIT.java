package com.example.todo.items.controller;


import com.example.todo.items.controller.dto.StatusUpdateDto;
import com.example.todo.CurrentDateTimeProvider;
import com.example.todo.items.controller.dto.ItemDetailsDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.OffsetDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerIT {

    private static final String DESCRIPTION = "description";
    private static final String DUE_DATE_TIME = "dueDateTime";
    private static final String STATUS = "status";
    private static final String ID = "id";
    private static final String FUTURE_DATE = "2023-10-22T15:35:30Z";
    private static final String PAST_DATE = "2023-10-20T15:35:30Z";
    private static final String NOW = "2023-10-21T12:00:00Z";
    @MockBean
    private CurrentDateTimeProvider currentDateTimeProvider;

    @BeforeEach
    void setUpCurrentDateTime() {
        Mockito.when(this.currentDateTimeProvider.now()).thenReturn(OffsetDateTime.parse(NOW));
    }

    @Test
    void shouldCreateItem() {
        String body = newCreateItemBody("test description", FUTURE_DATE);

        given().body(body).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then()
                .statusCode(equalTo(201))
                .body("id", notNullValue())
                .body(DESCRIPTION, equalTo("test description"))
                .body(DUE_DATE_TIME, equalTo(FUTURE_DATE))
                .body("created", equalTo(NOW))
                .body(STATUS, equalTo("NOT_DONE"));
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
                .then().statusCode(equalTo(201)).extract().path(ID);

        when().get("/items/{id}", createdId)
                .then()
                .statusCode(200)
                .body(ID, equalTo(createdId))
                .body(DESCRIPTION, equalTo("detailed description"))
                .body(DUE_DATE_TIME, equalTo(FUTURE_DATE))
                .body("created", equalTo(NOW))
                .body(STATUS, equalTo("NOT_DONE"));

    }


    @Test
    void shouldMarkItemAsDone() {
        String id = given().body(newCreateItemBody("Description", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateStatusBody(StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"));

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"));
    }

    @Test
    void shouldMarkItemAsNotDone() {
        String id = given().body(newCreateItemBody("Description", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateStatusBody(StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"));

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"));

        given().body(newUpdateStatusBody(StatusUpdateDto.NOT_DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("NOT_DONE"));

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("NOT_DONE"));
    }

    @Test
    void shouldUpdateDescription() {
        String id = given().body(newCreateItemBody("Before", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateDescriptionBody("After")).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(DESCRIPTION, equalTo("After"));

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(DESCRIPTION, equalTo("After"));
    }

    @Test
    void shouldRejectItemUpdateWithBlankDescription() {
        String id = given().body(newCreateItemBody("Before", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateDescriptionBody(" ")).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(400);

        given().body(newUpdateDescriptionBody("")).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(400);
    }

    @Test
    void shouldUpdateDescriptionAndStatusAtTheSameTime() {
        String id = given().body(newCreateItemBody("Before", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateItemBody("After", StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(DESCRIPTION, equalTo("After"))
                .body(STATUS, equalTo("DONE"));

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(DESCRIPTION, equalTo("After"))
                .body(STATUS, equalTo("DONE"));
    }

    @Test
    void shouldAddTheTimeOfCompletionWhenStatusIsChangedToDone() {
        String id = given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateStatusBody(StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"))
                .body("finished", equalTo(NOW));

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"))
                .body("finished", equalTo(NOW));
    }

    @Test
    void shouldClearTheTimeOfCompletionWhenStatusIsChangedToNotDone() {
        String id = given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateStatusBody(StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"))
                .body("finished", equalTo(NOW));


        given().body(newUpdateStatusBody(StatusUpdateDto.NOT_DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("NOT_DONE"))
                .body("finished", nullValue());

        when().get("/items/{id}", id)
                .then().statusCode(200)
                .body(STATUS, equalTo("NOT_DONE"))
                .body("finished", nullValue());
    }

    @Test
    void shouldGetAllItems() {
        ItemDetailsDto first = given().body(newCreateItemBody("First", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items").as(ItemDetailsDto.class);


        ItemDetailsDto second = given().body(newCreateItemBody("Second", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items").as(ItemDetailsDto.class);

        ItemDetailsDto[] allItems = when().get("/items").as(ItemDetailsDto[].class);
        assertThat(allItems).containsOnly(first, second);
    }

    @Test
    void shouldGetPastDueItems() {
        ItemDetailsDto first = given().body(newCreateItemBody("First", PAST_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items").as(ItemDetailsDto.class);


        given().body(newCreateItemBody("Second", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items").then().statusCode(201);

        ItemDetailsDto[] allItems = given().queryParam("status", "PAST_DUE").when().get("/items").as(ItemDetailsDto[].class);
        assertThat(allItems).containsOnly(first);
    }

    @Test
    void shouldReturnEmptyListOfAllItemsWhenThereAreNoItems() {
        ItemDetailsDto[] allItems = when().get("/items").as(ItemDetailsDto[].class);
        assertThat(allItems).isEmpty();
    }

    @Test
    void shouldGetOnlyItemsMarkedAsNotDone() {
        ItemDetailsDto notDone = given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items").as(ItemDetailsDto.class);

        given().body(newCreateItemBody("any", PAST_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(201);

        String doneId = given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        given().body(newUpdateStatusBody(StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", doneId)
                .then().statusCode(200)
                .body(STATUS, equalTo("DONE"))
                .body("finished", equalTo(NOW));

        ItemDetailsDto[] notDoneItems = given().queryParam("status", "NOT_DONE").contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/items").as(ItemDetailsDto[].class);

        assertThat(notDoneItems).containsOnly(notDone);
    }

    @Test
    void shouldGetOnlyItemsMarkedAsDone() {
        given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(201);

        String doneId = given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(equalTo(201)).extract().path(ID);

        ItemDetailsDto doneItem = given().body(newUpdateStatusBody(StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", doneId).as(ItemDetailsDto.class);

        ItemDetailsDto[] doneItems = given().queryParam("status", "DONE").contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/items").as(ItemDetailsDto[].class);

        assertThat(doneItems).containsOnly(doneItem);
    }

    @Test
    void shouldReturnBadRequestResponseWhenGetAllItemsRequestContainsUnknownStatus() {
        given().queryParam("status", "I-don't-exist").contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/items")
                .then().statusCode(400);
    }

    @Test
    void shouldNotBeAllowedToUpdatePastDueItem() {
        String id = given().body(newCreateItemBody("any", FUTURE_DATE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/items")
                .then().statusCode(201).extract().path("id");

        Mockito.when(this.currentDateTimeProvider.now()).thenReturn(OffsetDateTime.parse(FUTURE_DATE).plusDays(1));
        given().body(newUpdateItemBody("After", StatusUpdateDto.DONE)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/items/{id}", id)
                .then().statusCode(422);
    }

    private String newUpdateItemBody(String description, StatusUpdateDto status) {

        JSONObject requestParams = new JSONObject();
        try {
            if (description != null) {
                requestParams.put(DESCRIPTION, description);
            }
            if (status != null) {
                requestParams.put(STATUS, status.toString());
            }
        } catch (JSONException exception) {
            fail("Exception when constructing 'create item' request", exception);
        }
        return requestParams.toString();

    }

    private String newUpdateDescriptionBody(String description) {
        return this.newUpdateItemBody(description, null);
    }

    private String newUpdateStatusBody(StatusUpdateDto status) {
        return this.newUpdateItemBody(null, status);
    }


    private String newCreateItemBody(String description, String dueDateTime) {

        JSONObject requestParams = new JSONObject();
        try {
            if (description != null) {
                requestParams.put(DESCRIPTION, description);
            }
            if (dueDateTime != null) {
                requestParams.put(DUE_DATE_TIME, dueDateTime);
            }
        } catch (JSONException exception) {
            fail("Exception when constructing 'create item' request", exception);
        }
        return requestParams.toString();

    }
}
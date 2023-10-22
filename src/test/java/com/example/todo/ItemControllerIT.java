package com.example.todo;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ItemControllerIT {

	private static final String DATE_IN_THE_FUTURE = "2023-10-22T15:35:30Z";

	@Test
	void shouldCreateItem() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("description", "test description");
		requestParams.put("dueDateTime", DATE_IN_THE_FUTURE);

		given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then()
				.statusCode(equalTo(201))
				.body("id", notNullValue())
				.body("description", equalTo("test description"))
				.body("dueDateTime", equalTo(DATE_IN_THE_FUTURE))
				.body("created", notNullValue());
	}

	@Test
	void shouldReturnBadRequestResponseWhenDueDateTimeIsMissingInCreateRequest() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("description", "description");

		given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then()
				.statusCode(equalTo(400));
	}

	@Test
	void shouldReturnBadRequestResponseWhenDescriptionIsMissingInCreateRequest() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("dueDateTime", DATE_IN_THE_FUTURE);

		given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then()
				.statusCode(equalTo(400));
	}

	@Test
	void shouldReturnBadRequestResponseWhenDescriptionIsBlankInCreateRequest() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("dueDateTime", DATE_IN_THE_FUTURE);
		requestParams.put("description", "  ");

		given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then()
				.statusCode(equalTo(400));
	}

	@Test
	void shouldCreateAndGetDetailsOfItem() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("description", "detailed description");
		requestParams.put("dueDateTime", DATE_IN_THE_FUTURE);

		String createdId = given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then().statusCode(equalTo(201)).extract().path("id");

		when().get("/items/{id}", createdId)
				.then()
				.statusCode(200)
				.body("id", equalTo(createdId))
				.body("description", equalTo("detailed description"))
				.body("dueDateTime", equalTo(DATE_IN_THE_FUTURE))
				.body("created", notNullValue());
	}
}
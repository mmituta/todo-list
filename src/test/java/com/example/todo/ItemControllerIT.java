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

	@Test
	void shouldCreateItem() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("description", "test description");

		given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then()
				.statusCode(equalTo(201))
				.body("id", notNullValue())
				.body("description", equalTo("test description"));
	}
	@Test
	void shouldCreateAndGetDetailsOfItem() throws JSONException {
		JSONObject requestParams = new JSONObject();
		requestParams.put("description", "detailed description");

		String createdId = given().body(requestParams.toString()).contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("/items")
				.then().statusCode(equalTo(201)).extract().path("id");

		when().get("/items/{id}", createdId)
				.then()
				.statusCode(200)
				.body("id", equalTo(createdId))
				.body("description", equalTo("detailed description"));
	}
}

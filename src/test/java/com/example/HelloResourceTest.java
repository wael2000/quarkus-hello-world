package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class HelloResourceTest {

    @Test
    void helloEndpointReturnsHelloWorld() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello World 1.0"));
    }

    @Test
    void byeEndpointReturnsByeWorld() {
        given()
                .when().get("/bye")
                .then()
                .statusCode(200)
                .body(is("Bye World"));
    }
}

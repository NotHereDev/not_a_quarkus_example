package fr.not_here_dev.controllers

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.hamcrest.CoreMatchers.`is`
import org.jboss.resteasy.reactive.RestResponse.StatusCode


@QuarkusTest
@TestHTTPEndpoint(UserResource::class)
class UserResourceTest {
    @Test
    fun testIndex() {
        given()
            .`when`().get()
            .then()
            .statusCode(StatusCode.OK)
            .body(`is`("[]"))
    }

    @Test
    fun testShowFailed() {
        given()
            .pathParam("id", 1)
            .`when`().get("/{id}")
            .then()
            .statusCode(StatusCode.NOT_FOUND)
    }

    @Test
    fun testShowSuccess() {
        given()
            .pathParam("id", 1)
            .`when`().get("/{id}")
            .then()
            .statusCode(StatusCode.OK)
    }

    @Test
    fun testCreate() {
        given()
            .`when`().post()
            .then()
            .statusCode(StatusCode.BAD_REQUEST)
    }

    @Test
    fun testUpdate() {
        given()
            .pathParam("id", 1)
            .`when`().patch("/{id}")
            .then()
            .statusCode(StatusCode.NOT_FOUND)
    }

    @Test
    fun testDestroy() {
        given()
            .pathParam("id", 1)
            .`when`().delete("/{id}")
            .then()
            .statusCode(StatusCode.NOT_FOUND)
    }
}
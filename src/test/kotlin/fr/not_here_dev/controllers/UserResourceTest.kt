package fr.not_here_dev.controllers

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.hamcrest.CoreMatchers.`is`


@QuarkusTest
@TestHTTPEndpoint(UserResource::class)
class UserResourceTest {
    @Test
    fun testIndex() {
        given()
            .`when`().get()
            .then()
            .statusCode(200)
            .body(`is`("[]"))
    }

    @Test
    fun testShow() {
        given()
            .`when`().get("/{id}", 1)
            .then()
            .statusCode(404)
    }

    @Test
    fun testCreate() {
        given()
            .`when`().post()
            .then()
            .statusCode(400)
    }

    @Test
    fun testUpdate() {
        given()
            .`when`().patch("/{id}", 1)
            .then()
            .statusCode(404)
    }

    @Test
    fun testDestroy() {
        given()
            .`when`().delete("/{id}", 1)
            .then()
            .statusCode(404)
    }
}
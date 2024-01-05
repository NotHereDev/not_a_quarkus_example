package fr.not_here_dev.controllers.api

import fr.not_here_dev.entities.User
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.transaction.Transactional
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
    @Transactional
    fun testShow() {
        val user = User().apply {
            login = "user@example.com"
        }
        user.persistAndFlush()

        given()
            .pathParam("id", user.id)
            .`when`().get("/{id}")
            .then()
            .statusCode(StatusCode.OK)
    }

    @Test
    fun testCreate() {
        val user = User().apply {
            login = "user@example.com"
        }

        given()
            .body(user)
            .header("Content-Type", "application/json")
            .`when`().post()
            .then()
            .statusCode(StatusCode.CREATED)
    }

    @Test
    fun testUpdate() {
        given()
            .pathParam("id", 1)
            .body("{\"login\": \"user@example.com\"}")
            .header("Content-Type", "application/json")
            .`when`().patch("/{id}")
            .then()
            .statusCode(StatusCode.OK)
    }

    @Test
    fun testDestroy() {
        given()
            .pathParam("id", 1)
            .`when`().delete("/{id}")
            .then()
            .statusCode(StatusCode.OK)
    }
}
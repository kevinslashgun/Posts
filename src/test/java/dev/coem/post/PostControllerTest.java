package dev.coem.post;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Test
    public void shouldFindAllPosts() {
        final String endpoint = "/api/posts";
        final int expectedSize = 100;

        List<Post> response = given()
                .when()
                .get(endpoint)
                .then().log().ifError()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", Post.class);

        assertEquals(expectedSize, response.size(), "Expected 100 posts to be returned");
    }

    @Test
    public void shouldFindPostWhenValidPostId() {
        final String endpoint = "/api/posts/{id}";
        final int expectedPostId = 10;

        Post response = given()
                .pathParam("id", expectedPostId)
                .when()
                .get(endpoint)
                .then().log().ifError()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getObject(".", Post.class);

        assertEquals(expectedPostId, response.id(), "Expected post with id=" + expectedPostId + " to be returned");
    }
}
package api;

import api.Server;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import model.Course;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ServerTest {

    private final static String BASE_URL = "http://localhost:4567";
    private static final Gson gson = new Gson();

    @BeforeAll
    static void runApiServer() throws URISyntaxException {
        Server.main(null); // run the server
    }

    @AfterAll
    static void stopApiServer() {
        Server.stop();
    }

    @Test
    public void getCoursesWorks() throws UnirestException {
        final String URL = BASE_URL + "/api/courses";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void getCoursesGivenOfferingName() throws UnirestException {
        final String OFFERING_NAME = "EN.601.226";
        final String URL = BASE_URL + "/api/courses/" + OFFERING_NAME;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void getCoursesGivenOfferingNameNotInDatabase() throws UnirestException {
        final String OFFERING_NAME = "EN.000.999";
        final String URL = BASE_URL + "/api/courses/" + OFFERING_NAME;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(404, jsonResponse.getStatus());
    }

    @Test
    public void getCoursesGivenTitle() throws UnirestException {
        final String TITLE = "data";
        final String URL = BASE_URL + "/api/courses?title=" + TITLE;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void postCourseWorks() throws UnirestException {
        // This test will break if "EN.601.421" is already in database
        Course course = new Course("EN.601.421", "Object-Oriented Software Engineering");
        final String URL = BASE_URL + "/api/courses";
        HttpResponse<JsonNode> jsonResponse = Unirest.post(URL)
                .body(gson.toJson(course)).asJson();
        assertEquals(201, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void postCourseWithIncompleteData() throws UnirestException {
        Map<String, String> course = Map.of("title", "Made-up model.Course");
        final String URL = BASE_URL + "/api/courses";
        HttpResponse<JsonNode> jsonResponse = Unirest.post(URL)
                .body(gson.toJson(course)).asJson();
        assertEquals(500, jsonResponse.getStatus());
    }

    @Test
    public void postCourseThatAlreadyExist() throws UnirestException {
        // This test will break if "EN.601.226" is not in the database
        Course course = new Course("EN.601.226", "Data Structures");
        final String URL = BASE_URL + "/api/courses";
        HttpResponse<JsonNode> jsonResponse = Unirest.post(URL)
                .body(gson.toJson(course)).asJson();
        assertEquals(500, jsonResponse.getStatus());
    }

    @Test
    public void deleteCourseWorks() throws UnirestException {
        // This test will break if "EN.601.226" does not exists in database
        final String OFFERING_NAME = "EN.601.226";
        final String URL = BASE_URL + "/api/courses/" + OFFERING_NAME;
        HttpResponse<JsonNode> jsonResponse = Unirest.delete(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void deleteCourseNotInDatabase() throws UnirestException {
        // This test will break if "EN.000.999" exists in database
        final String OFFERING_NAME = "EN.000.999";
        final String URL = BASE_URL + "/api/courses/" + OFFERING_NAME;
        HttpResponse<JsonNode> jsonResponse = Unirest.delete(URL).asJson();
        assertEquals(404, jsonResponse.getStatus());
    }

    @Test
    public void putWorks() throws UnirestException{
        final String OFFERING_NAME = "EN.601.621";
        final String URL = BASE_URL + "/api/courses/" + OFFERING_NAME;
    }
}


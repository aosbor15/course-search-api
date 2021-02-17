package dao;

import dao.CourseDao;
import dao.Sql2oCourseDao;
import exceptions.DaoException;
import model.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oCourseDaoTest {
    private static Sql2o sql2o;
    private static List<Course> samples;
    private CourseDao courseDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        String databaseUrl = System.getenv("TEST_DATABASE_URL");
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        sql2o = new Sql2o(dbUrl, username, password);
    }

    @BeforeAll
    static void setSampleCourses() {
        samples = new ArrayList<>();
        samples.add(new Course("EN.500.112", "GATEWAY COMPUTING: JAVA"));
        samples.add(new Course("EN.601.220", "INTERMEDIATE PROGRAMMING"));
        samples.add(new Course("EN.601.226", "DATA STRUCTURES"));
        samples.add(new Course("EN.601.229", "COMPUTER SYSTEM FUNDAMENTALS"));
        samples.add(new Course("EN.601.230", "AUTOMATA and COMPUTATION THEORY"));
        samples.add(new Course("EN.601.315", "Databases"));
        samples.add(new Course("EN.601.476", "Machine Learning: Data to Models"));
        samples.add(new Course("EN.601.676", "Machine Learning: Data to Models"));
    }

    @BeforeEach
    void injectDependency() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS courses;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS courses("
                    + "offeringName VARCHAR(15) NOT NULL PRIMARY KEY,"
                    + "title VARCHAR(50) NOT NULL"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "INSERT INTO courses(offeringName, title) VALUES(:offeringName, :title);";
            for (Course course : samples) {
                conn.createQuery(sql).bind(course).executeUpdate();
            }
        }

        courseDao = new Sql2oCourseDao(sql2o);
    }

    @Test
    void doNothing() {

    }

    @Test
    @DisplayName("create works for valid input")
    void createNewCourse() {
        Course c1 = new Course("EN.601.421", "Object-Oriented Software Engineering");
        Course c2 = courseDao.create(c1.getOfferingName(), c1.getTitle());
        assertEquals(c1, c2);
    }

    @Test
    @DisplayName("create throws exception for duplicate course")
    void createThrowsExceptionDuplicateData() {
        assertThrows(DaoException.class, () -> {
            courseDao.create("EN.500.112", "GATEWAY COMPUTING: JAVA");
        });
    }

    @Test
    @DisplayName("create throws exception for invalid input")
    void createThrowsExceptionIncompleteData() {
        assertThrows(DaoException.class, () -> {
            courseDao.create(null, null);
        });
    }

    @Test
    @DisplayName("read a course given its offering name")
    void readCourseGivenOfferingName() {
        for (Course c2 : samples) {
            Course c1 = courseDao.read(c2.getOfferingName());
            assertEquals(c2, c1);
        }
    }

    @Test
    @DisplayName("read returns null given invalid offering name")
    void readCourseGivenInvalidOfferingName() {
        Course c1 = courseDao.read("EN.00.999");
        assertNull(c1);
    }

    @Test
    @DisplayName("read all the courses")
    void readAll() {
        List<Course> courses = courseDao.readAll();
        assertIterableEquals(samples, courses);
    }

    @Test
    @DisplayName("read all the courses that contain a query string in their title")
    void readAllGivenTitle() {
        String query = "data";
        List<Course> courses = courseDao.readAll(query);
        assertNotEquals(0, courses.size());
        for (Course course : courses) {
            assertTrue(course.getTitle().toLowerCase().contains(query.toLowerCase()));
        }
    }

    @Test
    @DisplayName("readAll(query) returns empty list when query not matches courses' title")
    void readAllGivenNonExistingTitle() {
        String query = "game";
        List<Course> courses = courseDao.readAll(query);
        assertEquals(0, courses.size());
    }

    @Test
    @DisplayName("updating a course works")
    void updateWorks() {
        String title = "Updated Title!";
        Course course = courseDao.update(samples.get(0).getOfferingName(), title);
        assertEquals(title, course.getTitle());
        assertEquals(samples.get(0).getOfferingName(), course.getOfferingName());
    }

    @Test
    @DisplayName("Update returns null for an invalid offeringCode")
    void updateReturnsNullInvalidCode() {
        Course course = courseDao.update("EN.000.999", "UpdatedTitle");
        assertNull(course);
    }

    @Test
    @DisplayName("Update throws exception for an invalid title")
    void updateThrowsExceptionInvalidTitle() {
        assertThrows(DaoException.class, () -> {
            courseDao.update(samples.get(0).getOfferingName(), null);
        });
    }


}

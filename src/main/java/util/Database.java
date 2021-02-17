package util;

import model.Course;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static util.DataStore.sampleCourses;

/**
 * A utility class with methods to establish JDBC connection, set schemas, etc.
 */
public final class Database {
    public static boolean USE_TEST_DATABASE = false;

    private Database() {
        // This class should not be instantiated.
    }

    /**
     * Connect to the database and build the tables with sample data for this application.
     * <p>
     * Caution: Use this to cleanup the database.
     * </p>
     *
     * @param args command-line arguments; not used here.
     * @throws URISyntaxException Checked exception thrown to indicate the provided database URL cannot be parsed as a
     *     URI reference.
     */
    public static void main(String[] args) throws URISyntaxException {
        Sql2o sql2o = getSql2o();
        createCoursesTableWithSampleData(sql2o, sampleCourses());
    }

    /**
     * Create and return a Sql2o object connected to the database pointed to by the DATABASE_URL.
     *
     * @return a Sql2o object connected to the database to be used in this application.
     * @throws URISyntaxException Checked exception thrown to indicate the provided database URL cannot be parsed as a
     *     URI reference.
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    public static Sql2o getSql2o() throws URISyntaxException, Sql2oException {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new URISyntaxException(databaseUrl, "DATABASE_URL is not set");
        }

        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = getDatabaseUrl();

        return new Sql2o(dbUrl, username, password);
    }

    /**
     * Create courses table schema and add sample CS courses to it.
     *
     * @param sql2o a Sql2o object connected to the database to be used in this application.
     * @param samples a list of sample CS courses.
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    public static void createCoursesTableWithSampleData(Sql2o sql2o, List<Course> samples) throws Sql2oException {
        try (Connection conn = sql2o.open()) {
            String sql = "CREATE TABLE IF NOT EXISTS courses("
                    + "offeringName VARCHAR(15) NOT NULL PRIMARY KEY,"
                    + "title VARCHAR(50) NOT NULL"
                    + ");";
            conn.createQuery(sql).executeUpdate();
            for (Course c : samples) {
                add(conn, c);
            }

        } catch (Sql2oException e) {
            e.printStackTrace();
        }
    }

    // Get either the test or the production Database URL
    private static String getDatabaseUrl() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new URISyntaxException(databaseUrl, "DATABASE_URL is not set");
        }

        URI dbUri = new URI(databaseUrl);
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return dbUrl;
    }

    // Add course to the database connected to the conn object.
    private static void add(Connection conn, Course course) throws Sql2oException {
        String sql = "INSERT INTO courses(offeringName, title) VALUES(:offeringName, :title);";
        conn.createQuery(sql)
                .bind(course)
                .executeUpdate();
    }
}

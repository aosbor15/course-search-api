import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import java.net.URI;
import java.net.URISyntaxException;

public class Demo {
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            String sql = "CREATE TABLE IF NOT EXISTS courses("
                    + "offeringName VARCHAR(15) NOT NULL PRIMARY KEY,"
                    + "title VARCHAR(50) NOT NULL"
                    + ");";
            conn.createQuery(sql).executeUpdate();
            Course course = new Course("EN.601.280", "Full-Stack JavaScript");
            add(conn, course);

        } catch (URISyntaxException | Sql2oException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws URISyntaxException, Sql2oException {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new URISyntaxException(databaseUrl, "DATABASE_URL is not set");
        }

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        Sql2o sql2o = new Sql2o(dbUrl, username, password);
        return sql2o.open();
    }

    private static void add(Connection conn, Course course) throws Sql2oException {
        String sql = "INSERT INTO courses(offeringName, title) VALUES(:offeringName, :title);";
        conn.createQuery(sql)
                .bind(course)
                .executeUpdate();
    }
}

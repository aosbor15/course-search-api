package dao;

import exceptions.DaoException;
import model.Course;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oCourseDao implements CourseDao {

    private final Sql2o sql2o;

    /**
     * Construct dao.Sql2oCourseDao.
     *
     * @param sql2o A Sql2o object is injected as a dependency;
     *   it is assumed sql2o is connected to a database that  contains a table called
     *   "courses" with two columns: "offeringName" and "title".
     */
    public Sql2oCourseDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Course create(String offeringName, String title) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO courses(offeringName, title) VALUES(:name, :title) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("name", offeringName)
                    .addParameter("title", title)
                    .executeAndFetchFirst(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Course read(String offeringName) throws DaoException {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM courses WHERE offeringName = :name;")
                    .addParameter("name", offeringName)
                    .executeAndFetchFirst(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a course with offeringName " + offeringName, ex);
        }
    }

    @Override
    public List<Course> readAll() throws DaoException {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM courses;").executeAndFetch(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read courses from the database", ex);
        }
    }

    @Override
    public List<Course> readAll(String titleQuery) throws DaoException {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM courses WHERE title LIKE '%" + titleQuery+"%';").executeAndFetch(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read courses from the database with given title", ex);
        }
    }

    @Override
    public Course update(String offeringName, String title) throws DaoException {
        String sql = "WITH updated AS ("
                + "UPDATE courses SET title = :title WHERE offeringName = :name RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("title", title)
                    .addParameter("name", offeringName)
                    .executeAndFetchFirst(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the course", ex);
        }
    }

    @Override
    public Course delete(String offeringName) throws DaoException {
        String sql = "WITH deleted AS ("
                + "DELETE FROM courses WHERE offeringName = :name RETURNING *"
                + ") SELECT * FROM deleted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("name", offeringName)
                    .executeAndFetchFirst(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the course", ex);
        }
    }
}

package dao;

import model.Course;
import exceptions.DaoException;

import java.util.List;

/**
 * Data Access Object for model.Course.
 */
public interface CourseDao {

    /**
     * Create a course.
     *
     * @param offeringName The course alphanumeric code.
     * @param title The course Title.
     * @return The course object created.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Course create(String offeringName, String title) throws DaoException;

    /**
     * Read a course provided its offeringName.
     *
     * @param offeringName The course alphanumeric code.
     * @return The course object read from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Course read(String offeringName) throws DaoException;

    /**
     * Read all courses from the database.
     *
     * @return All the courses in the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    List<Course> readAll() throws DaoException;

    /**
     * Read all courses from the database with title containing titleQuery.
     *
     * @param titleQuery A search term.
     * @return All courses retrieved.
     * @throws DaoException A generic exception for CRUD operations.
     */
    List<Course> readAll(String titleQuery) throws DaoException;

    /**
     * Update the title of a courses provided its offeringName.
     *
     * @param offeringName The course alphanumeric code.
     * @param title The course Title.
     * @return The updated course object.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Course update(String offeringName, String title) throws DaoException;

    /**
     * Delete a courses provided its offeringName.
     *
     * @param offeringName The course alphanumeric code.
     * @return The course object deleted from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Course delete(String offeringName) throws DaoException;
}

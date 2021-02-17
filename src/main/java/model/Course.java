package model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Represent a model.Course.
 */
public class Course {

    @SerializedName(value = "Offering Name")
    private final String offeringName;

    @SerializedName(value = "Title")
    private final String title;

    /**
     * Construct a model.Course.
     *
     * @param offeringName The course alphanumeric code.
     * @param title The course Title.
     */
    public Course(String offeringName, String title) {
        this.offeringName = offeringName;
        this.title = title;
    }

    /**
     * Get the title of the course.
     *
     * @return title of the course.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the course alphanumeric code.
     *
     * @return the course alphanumeric code.
     */
    public String getOfferingName() {
        return offeringName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offeringName, title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return offeringName.equals(course.offeringName) && title.equals(course.title);
    }

    @Override
    public String toString() {
        return offeringName + " " + title;
    }
}

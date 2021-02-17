package util;

import model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class with methods to create sample data.
 */
public final class DataStore {

    private DataStore() {
        // This class should not be instantiated.
    }

    /**
     * Create a list of sample CS courses.
     *
     * @return a list of sample CS courses.
     */
    public static List<Course> sampleCourses() {
        List<Course> samples = new ArrayList<>();
        samples.add(new Course("EN.500.112", "GATEWAY COMPUTING: JAVA"));
        samples.add(new Course("EN.601.220", "INTERMEDIATE PROGRAMMING"));
        samples.add(new Course("EN.601.226", "DATA STRUCTURES"));
        samples.add(new Course("EN.601.229", "COMPUTER SYSTEM FUNDAMENTALS"));
        samples.add(new Course("EN.601.230", "AUTOMATA and COMPUTATION THEORY"));
        samples.add(new Course("EN.601.315", "Databases"));
        samples.add(new Course("EN.601.476", "Machine Learning: Data to Models"));
        samples.add(new Course("EN.601.676", "Machine Learning: Data to Models"));
        return samples;
    }
}

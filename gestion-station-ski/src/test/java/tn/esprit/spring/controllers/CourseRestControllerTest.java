package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CourseRestControllerTest {

    @InjectMocks
    private CourseRestController courseRestController;

    @Mock
    private ICourseServices courseServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCourse() {
        // Given
        Course course = new Course();
        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        // When
        Course addedCourse = courseRestController.addCourse(course);

        // Then
        assertEquals(course, addedCourse);
        verify(courseServices, times(1)).addCourse(course);
    }

    @Test
    public void testGetAllCourses() {
        // Given
        List<Course> courses = new ArrayList<>();
        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        // When
        List<Course> retrievedCourses = courseRestController.getAllCourses();

        // Then
        assertEquals(courses, retrievedCourses);
        verify(courseServices, times(1)).retrieveAllCourses();
    }

    @Test
    public void testUpdateCourse() {
        // Given
        Course course = new Course();
        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        // When
        Course updatedCourse = courseRestController.updateCourse(course);

        // Then
        assertEquals(course, updatedCourse);
        verify(courseServices, times(1)).updateCourse(course);
    }

    @Test
    public void testGetById() {
        // Given
        Long courseId = 1L;
        Course course = new Course();
        when(courseServices.retrieveCourse(courseId)).thenReturn(course);

        // When
        Course retrievedCourse = courseRestController.getById(courseId);

        // Then
        assertEquals(course, retrievedCourse);
        verify(courseServices, times(1)).retrieveCourse(courseId);
    }
}

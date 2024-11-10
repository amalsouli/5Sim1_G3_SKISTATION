package tn.esprit.spring.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

    @Autowired
    private ICourseRepository courseRepository;

    @Test
    @Description("Repository can save a Cour")
    void TestRepositoryCourse_save(){
        //  Arrage
        Set<Registration> registrations = new HashSet<>();
        Course newCourse = new Course(0L,5, TypeCourse.INDIVIDUAL, Support.SKI, 150.0f, 3,registrations);

        // Act
        Course savedCourse = courseRepository.save(newCourse);

        // Assert
        Assertions.assertNotNull(savedCourse);
        Assertions.assertEquals(5,savedCourse.getLevel());
        Assertions.assertEquals(TypeCourse.INDIVIDUAL,savedCourse.getTypeCourse());
        Assertions.assertEquals(Support.SKI,savedCourse.getSupport());
        Assertions.assertEquals(150.0f,savedCourse.getPrice());
        Assertions.assertEquals(3,savedCourse.getTimeSlot());

    }

    @Test
    @Description("Repository can read all Cours")
    void TestRepositoryCourse_findAll(){
        // Arrage
        Set<Registration> registrations = new HashSet<>();
        Course newCourse = new Course(0L,5, TypeCourse.INDIVIDUAL, Support.SKI, 150.0f, 3,registrations);
        Course newCourse1 = new Course(0L,5, TypeCourse.INDIVIDUAL, Support.SKI, 150.0f, 3,registrations);

        courseRepository.save(newCourse);
        courseRepository.save(newCourse1);
        // Act
        List<Course> courses = courseRepository.findAll();

        // Assert
        Assertions.assertNotNull(courses);
        Assertions.assertEquals(2,courses.size());
    }
}
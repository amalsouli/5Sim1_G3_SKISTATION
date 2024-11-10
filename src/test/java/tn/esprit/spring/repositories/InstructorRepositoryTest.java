package tn.esprit.spring.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InstructorRepositoryTest {

    @Autowired
    private IInstructorRepository instructorRepository;

    @Test
    @Description("Repository can save a Instructor")
    void TestRepositoryInstructor_save(){
        Set<Course> courses = new HashSet<>();
        LocalDate dateOfHire = LocalDate.of(1990, 1, 1);
        //  Arrage
        Instructor newInstructor = new Instructor(5L,"sami","Miled",dateOfHire,courses);



        //Act
        Instructor savedInstructor = instructorRepository.save(newInstructor);


        //Assert
        Assertions.assertNotNull(savedInstructor);
        Assertions.assertEquals("sami",savedInstructor.getFirstName());
        Assertions.assertEquals("Miled",savedInstructor.getLastName());
        Assertions.assertEquals(dateOfHire,savedInstructor.getDateOfHire());

    }
    @Test
    @Description("Repository can read all Instructors")
    void TestRepositoryInstructor_findAll(){
        // Arrage
        Set<Course> courses = new HashSet<>();
        LocalDate dateofHire = LocalDate.of(1990, 1, 1);
        Instructor newInstructor = new Instructor(5L,"sami","Miled",dateofHire,courses);
        Instructor newInstructor1 = new Instructor(5L,"sami","Miled",dateofHire,courses);

        instructorRepository.save(newInstructor);
        instructorRepository.save(newInstructor1);
        // Act
        Iterable<Instructor> instructors = instructorRepository.findAll();

        // Assert
        Assertions.assertNotNull(instructors);
    }
    @Test
    void InstructorRepository_findByNum(){
        // Arrage
        Set<Course> courses = new HashSet<>();
        LocalDate dateofHire = LocalDate.of(1990, 1, 1);
        Instructor newInstructor = new Instructor(5L,"sami","Miled",dateofHire,courses);
        Instructor savedInstructor = instructorRepository.save(newInstructor);

        // Act
        Instructor foundInstructor = instructorRepository.findById(savedInstructor.getNumInstructor()).get();

        // Assert
        Assertions.assertNotNull(foundInstructor);
        Assertions.assertEquals(savedInstructor.getNumInstructor(),foundInstructor.getNumInstructor());
        Assertions.assertEquals(savedInstructor.getFirstName(),foundInstructor.getFirstName());
        Assertions.assertEquals(savedInstructor.getLastName(),foundInstructor.getLastName());
        Assertions.assertEquals(savedInstructor.getDateOfHire(),foundInstructor.getDateOfHire());
    }

    @Test
    void InstructorRepository_findByName(){
        // Arrage
        Set<Course> courses = new HashSet<>();
        LocalDate dateofHire = LocalDate.of(1990, 1, 1);
        Instructor newInstructor = new Instructor(5L,"sami","Miled",dateofHire,courses);
        Instructor savedInstructor = instructorRepository.save(newInstructor);

        // Act
        Instructor foundInstructor = instructorRepository.findById(savedInstructor.getNumInstructor()).get();

        // Assert
        Assertions.assertNotNull(foundInstructor);
        Assertions.assertEquals(savedInstructor.getNumInstructor(),foundInstructor.getNumInstructor());
        Assertions.assertEquals(savedInstructor.getFirstName(),foundInstructor.getFirstName());
        Assertions.assertEquals(savedInstructor.getLastName(),foundInstructor.getLastName());
        Assertions.assertEquals(savedInstructor.getDateOfHire(),foundInstructor.getDateOfHire());
    }
    @Test
    void InstructorRepository_findByLastName(){
        // Arrage
        Set<Course> courses = new HashSet<>();
        LocalDate dateofHire = LocalDate.of(1990, 1, 1);
        Instructor newInstructor = new Instructor(5L,"sami","Miled",dateofHire,courses);
        Instructor savedInstructor = instructorRepository.save(newInstructor);

        // Act
        Instructor foundInstructor = instructorRepository.findById(savedInstructor.getNumInstructor()).get();

        // Assert
        Assertions.assertNotNull(foundInstructor);
        Assertions.assertEquals(savedInstructor.getNumInstructor(),foundInstructor.getNumInstructor());
        Assertions.assertEquals(savedInstructor.getFirstName(),foundInstructor.getFirstName());
        Assertions.assertEquals(savedInstructor.getLastName(),foundInstructor.getLastName());
        Assertions.assertEquals(savedInstructor.getDateOfHire(),foundInstructor.getDateOfHire());
    }

}
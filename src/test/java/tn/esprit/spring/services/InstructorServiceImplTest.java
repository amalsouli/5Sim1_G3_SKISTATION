package tn.esprit.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class InstructorServiceImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @InjectMocks
    private InstructorServicesImpl instructorService;

    @BeforeEach
    void setUp() {
        // Initializes the @Mock annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Service can show all instructors")
    void testReadALLInstructor() {
        List<Instructor> result = instructorService.retrieveAllInstructors();
        Assertions.assertEquals(0, result.size());
    }
    @Test
    @DisplayName("Service can add Instructor")
    void testAddInstructor(){
        Set<Course> courses = new HashSet<>();
        LocalDate dateOfHire = LocalDate.of(1990, 1, 1);
        Instructor newInstructor = new Instructor(0L,"sami","Miled",dateOfHire,courses);
        when(instructorRepository.save(newInstructor)).thenReturn(newInstructor);
        Instructor savedInstructor = instructorService.addInstructor(newInstructor);

        assertNotNull(savedInstructor);
        assertEquals(0L, savedInstructor.getNumInstructor());
        assertEquals("sami", savedInstructor.getFirstName());
        assertEquals("Miled", savedInstructor.getLastName());
        assertEquals(dateOfHire, savedInstructor.getDateOfHire());
        assertEquals(courses, savedInstructor.getCourses());

       List<Instructor> newInstructors = new ArrayList<>();
        newInstructors.add(newInstructor);
        when(instructorRepository.findAll()).thenReturn(newInstructors);
        List<Instructor> result = instructorService.retrieveAllInstructors();
        assertEquals(1, result.size());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedInstructor, result.get(0));
    }

    @Test
    @DisplayName("Service can update Instructor")
    void testUpdateInstructor(){
        Set<Course> courses = new HashSet<>();
        LocalDate dateOfHire = LocalDate.of(1990, 1, 1);
        Instructor existingInstructor = new Instructor(0L,"sami","Miled",dateOfHire,courses);
        when(instructorRepository.save(existingInstructor)).thenReturn(existingInstructor);

        Instructor updateInstructor = instructorService.updateInstructor(existingInstructor);


        assertNotNull(updateInstructor);
        assertEquals(0L, updateInstructor.getNumInstructor());
        assertEquals("sami", updateInstructor.getFirstName());
        assertEquals("Miled", updateInstructor.getLastName());
        assertEquals(dateOfHire, updateInstructor.getDateOfHire());
        assertEquals(courses, updateInstructor.getCourses());

    }

}

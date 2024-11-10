package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;
    private Registration registration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample objects
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2005, 1, 1)); // Sample age

        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course.setSupport(Support.SKI);

        registration = new Registration();
        registration.setNumWeek(1);
        registration.setSkier(skier);
        registration.setCourse(course);
    }

    @Test
    void testAddRegistration() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        assertNotNull(result);
        assertEquals(result.getSkier().getNumSkier(), skier.getNumSkier());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testAssignCourse() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        assertNotNull(result);
        assertEquals(result.getCourse().getNumCourse(), course.getNumCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testDuplicateRegistration() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(1L);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNull(result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAddCollectiveChildrenRegistration() {
        skier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Skier is 14 years old
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(5L);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNotNull(result);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testFullCollectiveCourse() {
        skier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Skier is 14 years old
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(6L); // Full course

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        assertNull(result);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testInstructorNumWeeks() {
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(1L, Support.SKI))
                .thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
    }
}

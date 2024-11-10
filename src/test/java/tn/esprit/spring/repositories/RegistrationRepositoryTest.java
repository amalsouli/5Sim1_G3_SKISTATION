package tn.esprit.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Arrays;

class RegistrationRepositoryTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Registration registration;
    private Skier skier;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        skier = new Skier();
        skier.setNumSkier(1L);

        course = new Course();
        course.setNumCourse(1L);

        registration = new Registration();
        registration.setNumWeek(1);
        registration.setSkier(skier);
        registration.setCourse(course);
    }

    @Test
    void saveReg_ReturnNotNull() {
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationRepository.save(registration);

        assertNotNull(result);
        assertEquals(registration.getNumWeek(), result.getNumWeek());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void findById_ReturnNotNull() {
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        Optional<Registration> result = registrationRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(registration.getNumWeek(), result.get().getNumWeek());
        verify(registrationRepository, times(1)).findById(1L);
    }

    @Test
    void countDistinct_ReturnsZero() {
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(0L);

        long count = registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L);

        assertEquals(0L, count);
        verify(registrationRepository, times(1)).countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L);
    }

    @Test
    void countByCourse_ReturnsFive() {
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(5L);

        long count = registrationRepository.countByCourseAndNumWeek(course, 1);

        assertEquals(5L, count);
        verify(registrationRepository, times(1)).countByCourseAndNumWeek(course, 1);
    }

    @Test
    void numWeeksBySupport_ReturnsNotNull() {
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(1L, Support.SKI)).thenReturn(Arrays.asList(1, 2, 3));

        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(registrationRepository, times(1)).numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
    }
}

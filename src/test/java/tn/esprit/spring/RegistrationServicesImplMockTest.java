package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationServicesImplMockTest {

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

        // Initialiser les objets pour les tests
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2000, 1, 1));  // Age de 24 ans

        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);  // Type de cours individuel

        registration = new Registration();
        registration.setNumWeek(2);  // Semaine 2 de l'année
    }

    @Test
    void addRegistrationAndAssignToSkier_ShouldAssignRegistrationToSkier() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(java.util.Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getSkier().getNumSkier());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void assignRegistrationToCourse_ShouldAssignRegistrationToCourse() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(java.util.Optional.of(registration));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationServices.assignRegistrationToCourse(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getCourse().getNumCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse_ShouldAssignBoth() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(java.util.Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        // Le mock de countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse retourne un long
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(2, 1L, 1L)).thenReturn(0L);

        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getSkier().getNumSkier());
        assertEquals(1L, result.getCourse().getNumCourse());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse_ShouldReturnNullIfAlreadyRegistered() {
        // Given
        when(skierRepository.findById(1L)).thenReturn(java.util.Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        // Mock de countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse pour simuler que l'utilisateur est déjà inscrit
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(2, 1L, 1L)).thenReturn(1L);

        // When
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Then
        assertNull(result);
        verify(registrationRepository, times(0)).save(any(Registration.class));
    }

    @Test
    void numWeeksCourseOfInstructorBySupport_ShouldReturnWeeks() {
        // Given
        Support support = Support.SKI;  // Exemple de support
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(1L, support)).thenReturn(List.of(1, 2, 3));

        // When
        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(1L, support);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }
}

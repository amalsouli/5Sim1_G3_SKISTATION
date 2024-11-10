package tn.esprit.spring;

        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.*;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import tn.esprit.spring.controllers.RegistrationRestController;
        import tn.esprit.spring.entities.Registration;
        import tn.esprit.spring.entities.Support;
        import tn.esprit.spring.services.IRegistrationServices;

        import java.util.Arrays;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class RegistrationRestControllerTest {


    @Mock
    private IRegistrationServices registrationServices;

    @InjectMocks
    private RegistrationRestController registrationRestController;

    private Registration registration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialiser l'objet Registration pour les tests
        registration = new Registration();
        registration.setNumWeek(2);  // Par exemple, semaine 2
    }

    @Test
    void addAndAssignToSkier_ShouldReturnRegistration() {
        // Given
        when(registrationServices.addRegistrationAndAssignToSkier(any(Registration.class), eq(1L))).thenReturn(registration);

        // When
        Registration result = registrationRestController.addAndAssignToSkier(registration, 1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getNumWeek());
        verify(registrationServices, times(1)).addRegistrationAndAssignToSkier(any(Registration.class), eq(1L));
    }

    @Test
    void assignToCourse_ShouldReturnRegistration() {
        // Given
        when(registrationServices.assignRegistrationToCourse(1L, 1L)).thenReturn(registration);

        // When
        Registration result = registrationRestController.assignToCourse(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getNumWeek());
        verify(registrationServices, times(1)).assignRegistrationToCourse(1L, 1L);
    }

    @Test
    void addAndAssignToSkierAndCourse_ShouldReturnRegistration() {
        // Given
        when(registrationServices.addRegistrationAndAssignToSkierAndCourse(any(Registration.class), eq(1L), eq(1L))).thenReturn(registration);

        // When
        Registration result = registrationRestController.addAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getNumWeek());
        verify(registrationServices, times(1)).addRegistrationAndAssignToSkierAndCourse(any(Registration.class), eq(1L), eq(1L));
    }

    @Test
    void numWeeksCourseOfInstructorBySupport_ShouldReturnWeeks() {
        // Given
        List<Integer> expectedWeeks = Arrays.asList(1, 2, 3);
        when(registrationServices.numWeeksCourseOfInstructorBySupport(1L, Support.SKI)).thenReturn(expectedWeeks);

        // When
        List<Integer> result = registrationRestController.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
        verify(registrationServices, times(1)).numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
    }
}

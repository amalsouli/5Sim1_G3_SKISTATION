package tn.esprit.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierService;

    private Skier skier;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        // Initializes the @Mock annotations
        MockitoAnnotations.openMocks(this);

        // Set up a subscription and skier for tests
        subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        skier = new Skier();
        skier.setSubscription(subscription);
    }

    @Test
    @DisplayName("Service can show all Skiers")
    void testRetrieveAllSkiers() {
        List<Skier> skierList = new ArrayList<>();
        when(skierRepository.findAll()).thenReturn(skierList);

        List<Skier> result = skierService.retrieveAllSkiers();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Service can add Skier")
    void testAddSkier() {
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier savedSkier = skierService.addSkier(skier);

        assertNotNull(savedSkier);
        assertEquals(subscription, savedSkier.getSubscription());

        List<Skier> newSkierList = new ArrayList<>();
        newSkierList.add(savedSkier);

        when(skierRepository.findAll()).thenReturn(newSkierList);

        List<Skier> result = skierService.retrieveAllSkiers();
        Assertions.assertEquals(1, result.size());
        assertEquals(savedSkier, result.get(0));
    }

    @Test
    @DisplayName("Service can update Skier")
    void testUpdateSkier() {
        // Mock existing Skier
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier updatedSkier = skierService.retrieveSkier(1L);
        updatedSkier.setSubscription(subscription); // Simulate an update

        Skier savedSkier = skierService.addSkier(updatedSkier);

        verify(skierRepository, times(1)).save(updatedSkier);
        assertNotNull(savedSkier);
        assertEquals(skier, savedSkier);
    }

    @Test
    @DisplayName("Service can retrieve a Skier by ID")
    void testRetrieveSkierById() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier result = skierService.retrieveSkier(1L);
        assertNotNull(result);
        assertEquals(skier, result);
    }

    @Test
    @DisplayName("Service can remove a Skier")
    void testRemoveSkier() {
        doNothing().when(skierRepository).deleteById(1L);

        skierService.removeSkier(1L);
        verify(skierRepository, times(1)).deleteById(1L);
    }
}

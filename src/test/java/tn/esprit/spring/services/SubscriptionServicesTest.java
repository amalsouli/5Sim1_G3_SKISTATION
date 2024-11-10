package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServicesTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;


    @Mock
    private ISkierRepository skierRepository;


    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddSubscription_Annual() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.of(2024, 1, 1));

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertEquals(LocalDate.of(2025, 1, 1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testAddSubscription_Monthly() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        subscription.setStartDate(LocalDate.of(2024, 1, 1));

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertEquals(LocalDate.of(2024, 2, 1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById_Found() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveSubscriptionById_NotFound() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNull(result);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSubscriptionByType() {
        Set<Subscription> subscriptions = Set.of(new Subscription(), new Subscription());

        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(TypeSubscription.ANNUAL);

        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(TypeSubscription.ANNUAL);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<Subscription> subscriptions = List.of(new Subscription(), new Subscription());

        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }
}


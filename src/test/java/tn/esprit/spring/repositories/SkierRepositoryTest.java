package tn.esprit.spring.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SkierRepositoryTest {

    @Autowired
    private ISkierRepository skierRepository;

    @BeforeEach
    void setUp() {
        // Clean up the repository before each test
        skierRepository.deleteAll();
    }

    @Test
    public void testFindBySubscription_TypeSub() {
        // Given
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        Skier skier1 = new Skier();
        skier1.setSubscription(subscription);

        Skier skier2 = new Skier();
        skier2.setSubscription(subscription);

        skierRepository.save(skier1);
        skierRepository.save(skier2);

        // When
        List<Skier> result = skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(skier1, skier2);
    }

    @Test
    public void testFindBySubscription() {
        // Given
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());

        Skier skier = new Skier();
        skier.setSubscription(subscription);
        skierRepository.save(skier);

        // When
        Skier result = skierRepository.findBySubscription(subscription);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(skier);
    }
}

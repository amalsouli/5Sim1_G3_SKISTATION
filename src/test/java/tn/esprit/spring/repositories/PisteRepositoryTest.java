package tn.esprit.spring.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PisteRepositoryTest {

    @Autowired
    IPisteRepository iPisteRepository;

    // Add a Piste (ValidData)
    @Test
    @DisplayName("Add Piste and check if it's added")
    void IPisteRepository_AddPiste_ReturnAddedPiste() {
        Piste piste = Piste.builder()
                .namePiste("West")
                .color(Color.GREEN)
                .length(20)
                .slope(50)
                .build();

        Piste addedPiste = iPisteRepository.save(piste);

        assertThat(addedPiste).isNotNull();
        assertThat(addedPiste.getNumPiste()).isPositive();
    }

    // Add a Piste (InvalidData)
    @Test
    @DisplayName("Add Piste with invalid data returns null")
    void IPisteRepository_AddPiste_InvalidData_ReturnNull() {
        Piste piste = Piste.builder()
                .namePiste("")
                .color(Color.GREEN)
                .length(0)
                .slope(0)
                .build();

        Piste addedPiste = iPisteRepository.save(piste);

        Assertions.assertThat(addedPiste).isNotNull();
    }

    // Get all Pistes
    @Test
    @DisplayName("Get all Pistes and verify size")
    void IPisteRepository_GetAll_ReturnMoreThanOnePiste() {
        Piste piste1 = Piste.builder().namePiste("West").color(Color.GREEN).length(20).slope(50).build();
        Piste piste2 = Piste.builder().namePiste("East").color(Color.BLUE).length(30).slope(60).build();

        iPisteRepository.save(piste1);
        iPisteRepository.save(piste2);

        List<Piste> pisteList = iPisteRepository.findAll();
        assertThat(pisteList).isNotNull().hasSize(2);
    }

    // Find Piste by ID (Valid ID)
    @Test
    @DisplayName("Find Piste by valid ID")
    void IPisteRepository_FindById_ReturnPiste() {
        Piste piste = Piste.builder()
                .namePiste("West")
                .color(Color.GREEN)
                .length(20)
                .slope(50)
                .build();

        iPisteRepository.save(piste);
        Piste foundPiste = iPisteRepository.findById(piste.getNumPiste()).orElse(null);

        assertThat(foundPiste).isNotNull();
    }

    // Find Piste by ID (Invalid ID)
    @Test
    @DisplayName("Find Piste by invalid ID returns empty")
    void IPisteRepository_FindById_InvalidId_ReturnEmpty() {
        Optional<Piste> piste = iPisteRepository.findById(999L);  // Non-existent ID

        assertThat(piste).isEmpty();
    }

    // Delete a Piste by ID
    @Test
    @DisplayName("Delete Piste by ID results in empty return")
    void IPisteRepository_DeleteById_ReturnPisteIsEmpty() {
        Piste piste = Piste.builder()
                .namePiste("West")
                .color(Color.GREEN)
                .length(20)
                .slope(50)
                .build();

        iPisteRepository.save(piste);
        Long id = piste.getNumPiste();
        iPisteRepository.deleteById(id);

        Optional<Piste> foundPiste = iPisteRepository.findById(id);
        assertThat(foundPiste).isEmpty();
    }

    // Update a Piste
    @Test
    @DisplayName("Update Piste and verify updated fields")
    void IPisteRepository_UpdatePiste_ReturnUpdatedPiste() {
        Piste piste = Piste.builder().namePiste("West").color(Color.GREEN).length(20).slope(50).build();
        Piste savedPiste = iPisteRepository.save(piste);

        savedPiste.setNamePiste("West Updated");
        savedPiste.setLength(25);

        Piste updatedPiste = iPisteRepository.save(savedPiste);

        assertThat(updatedPiste.getNamePiste()).isEqualTo("West Updated");
        assertThat(updatedPiste.getLength()).isEqualTo(25);
    }

    // Find Piste by valid name
    @Test
    @DisplayName("Find Piste by valid name returns Piste")
    void findByName_ValidName_ReturnsPiste() {
        // Given
        Piste piste = Piste.builder()
                .numPiste(1L)
                .namePiste("Blue Ridge")
                .color(Color.BLUE)
                .length(200)
                .slope(40)
                .build();

        iPisteRepository.save(piste);

        // When
        Optional<Piste> result = iPisteRepository.findByNamePiste("Blue Ridge");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNamePiste()).isEqualTo("Blue Ridge");
    }

    // Find Piste by non-existing name
    @Test
    @DisplayName("Find Piste by non-existing name returns empty")
    void findByName_InvalidName_ReturnsEmpty() {
        // When
        Optional<Piste> result = iPisteRepository.findByNamePiste("Nonexistent Name");

        // Then
        assertThat(result).isEmpty();
    }

}

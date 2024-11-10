package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = Piste.builder()
                .numPiste(1L)
                .namePiste("Green Hill")
                .color(Color.GREEN)
                .length(150)
                .slope(30)
                .build();
    }

    // Test case for adding a Piste
    @Test
    @DisplayName("Add Piste and return it")
    void addPiste_ReturnsPiste() {
        given(pisteRepository.save(piste)).willReturn(piste);

        Piste addedPiste = pisteServices.addPiste(piste);

        assertThat(addedPiste).isNotNull();
        assertThat(addedPiste.getNamePiste()).isEqualTo("Green Hill");
    }

    // Test case for retrieving all Pistes
    @Test
    @DisplayName("Get all Pistes")
    void retrieveAllPistes_ReturnsListOfPistes() {
        List<Piste> pistes = new ArrayList<>();
        pistes.add(piste);

        given(pisteRepository.findAll()).willReturn(pistes);

        List<Piste> retrievedPistes = pisteServices.retrieveAllPistes();

        assertThat(retrievedPistes).isNotNull().hasSize(1);
        assertThat(retrievedPistes.get(0).getNamePiste()).isEqualTo("Green Hill");
    }

    // Test case for retrieving a Piste by ID (valid)
    @Test
    @DisplayName("Get Piste by valid ID")
    void retrievePiste_ValidId_ReturnsPiste() {
        given(pisteRepository.findById(1L)).willReturn(Optional.of(piste));

        Piste retrievedPiste = pisteServices.retrievePiste(1L);

        assertThat(retrievedPiste).isNotNull();
        assertThat(retrievedPiste.getNamePiste()).isEqualTo("Green Hill");
    }

    // Test case for retrieving a Piste by ID (invalid)
    @Test
    @DisplayName("Get Piste by invalid ID returns null")
    void retrievePiste_InvalidId_ReturnsNull() {
        given(pisteRepository.findById(999L)).willReturn(Optional.empty());

        Piste retrievedPiste = pisteServices.retrievePiste(999L);

        assertThat(retrievedPiste).isNull();
    }

    // Test case for deleting a Piste by ID
    @Test
    @DisplayName("Delete Piste by ID")
    void removePiste_ValidId() {
        given(pisteRepository.existsById(1L)).willReturn(true);

        pisteServices.removePiste(1L);

        verify(pisteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete non-existent Piste should throw exception")
    void removePiste_InvalidId_ThrowsException() {
        // Given that the Piste does not exist
        given(pisteRepository.existsById(999L)).willReturn(false);

        // When attempting to remove a Piste with a non-existent ID
        // Then an IllegalArgumentException should be thrown
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> pisteServices.removePiste(999L));

        // Assert the exception message
        assertThat(thrown.getMessage()).isEqualTo("Piste not found");
    }

    // Test case for retrieving a Piste by Name (valid)
    @Test
    @DisplayName("Get Piste by valid name")
    void retrievePiste_ValidName_ReturnsPiste() {
        given(pisteRepository.findByNamePiste("Green Hill")).willReturn(Optional.of(piste));

        Piste retrievedPiste = pisteServices.retrievePisteByName("Green Hill");

        assertThat(retrievedPiste).isNotNull();
        assertThat(retrievedPiste.getNamePiste()).isEqualTo("Green Hill");
    }


    // Test case for retrieving a Piste by Name (invalid)
    @Test
    @DisplayName("Get Piste by invalid Name returns null")
    void retrievePiste_InvalidName_ReturnsNull() {
        given(pisteRepository.findByNamePiste("Ariana")).willReturn(Optional.empty());

        Piste retrievedPiste = pisteServices.retrievePisteByName("Ariana");

        assertThat(retrievedPiste).isNull();
    }
    // Test case for updating a Piste (valid)
    @Test
    @DisplayName("Update Piste and return updated Piste")
    void updatePiste_ReturnsUpdatedPiste() {
        Piste newPisteData = Piste.builder()
                .namePiste("Blue Ridge")
                .color(Color.BLUE)
                .length(200)
                .slope(40)
                .build();

        given(pisteRepository.findById(1L)).willReturn(Optional.of(new Piste())); // Simulate existing Piste
        given(pisteRepository.save(any(Piste.class))).willReturn(newPisteData);

        Piste result = pisteServices.updatePiste(1L, newPisteData); // Pass both parameters

        assertThat(result).isNotNull();
        assertThat(result.getNamePiste()).isEqualTo("Blue Ridge");
    }


    // Test case for updating a Piste (invalid)
    @Test
    @DisplayName("Update non-existent Piste should throw exception")
    void updatePiste_InvalidId_ThrowsException() {
        // Arrange
        Long nonExistentPisteId = 999L; // ID that does not exist
        Piste newPisteData = Piste.builder()
                .namePiste("Blue Ridge") // other necessary fields
                .build();

        // Mock the repository to return empty when searching for the non-existent ID
        given(pisteRepository.findById(nonExistentPisteId)).willReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            pisteServices.updatePiste(nonExistentPisteId, newPisteData); // Pass the ID and new data
        });

        assertThat(thrown.getMessage()).isEqualTo("Piste not found");
    }

    @Test
    @DisplayName("Retrieve Piste by valid name returns Piste")
    void retrievePisteByName_ValidName_ReturnsPiste() {
        // Given
        String namePiste = "Blue Ridge";
        Piste piste1 = Piste.builder()
                .numPiste(1L)
                .namePiste(namePiste)
                .color(Color.BLUE)
                .length(200)
                .slope(40)
                .build();

        given(pisteRepository.findByNamePiste(namePiste)).willReturn(Optional.of(piste1));

        // When
        Piste result = pisteServices.retrievePisteByName(namePiste);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNamePiste()).isEqualTo(namePiste);
    }

    @Test
    @DisplayName("Retrieve Piste by non-existing name returns null")
    void retrievePisteByName_InvalidName_ReturnsNull() {
        // Given
        String namePiste = "Nonexistent Name";
        given(pisteRepository.findByNamePiste(namePiste)).willReturn(Optional.empty());

        // When
        Piste result = pisteServices.retrievePisteByName(namePiste);

        // Then
        assertThat(result).isNull();
    }

}

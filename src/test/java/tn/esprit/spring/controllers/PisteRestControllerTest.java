package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

@WebMvcTest(PisteRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class PisteRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IPisteServices pisteServices;

    @Autowired
    ObjectMapper objectMapper;

    private Piste piste;

    @BeforeEach
    public void init() {
        // Initialize a sample Piste object for testing
        piste = Piste.builder()
                .numPiste(1L)
                .namePiste("Green Hill")
                .color(Color.GREEN)
                .length(150)
                .slope(30)
                .build();
    }

    // Add a Piste (ValidData)
    @Test
    @DisplayName("Add valid Piste returns created status")
    void addPiste_ValidData_ReturnsCreated() throws Exception {
        given(pisteServices.addPiste(ArgumentMatchers.any())).willReturn(piste);

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piste)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.namePiste").value("Green Hill"))
                .andExpect(jsonPath("$.color").value(Color.GREEN.toString()));
    }

    // Add a Piste (InvalidData)
    @Test
    @DisplayName("Add invalid Piste returns bad request")
    void addPiste_InvalidData_ReturnsBadRequest() throws Exception {
        // Example of invalid data
        String invalidPisteJson = "{\"namePiste\":\"\",\"color\":\"\",\"length\":-10,\"slope\":-5}";

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPisteJson))
                .andExpect(status().isBadRequest());
    }

    // Get all Pistes
    @Test
    @DisplayName("Get all Pistes returns list of Pistes")
    void getAllPistes_ReturnsPistes() throws Exception {
        List<Piste> pistes = new ArrayList<>();
        pistes.add(piste);

        given(pisteServices.retrieveAllPistes()).willReturn(pistes);

        mockMvc.perform(get("/piste/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].namePiste").value("Green Hill"));
    }

    // Find Piste by ID (Valid ID)
    @Test
    @DisplayName("Get Piste by valid ID returns Piste")
    void getById_ValidId_ReturnsPiste() throws Exception {
        given(pisteServices.retrievePiste(1L)).willReturn(piste);

        mockMvc.perform(get("/piste/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namePiste").value("Green Hill"));
    }

    // Find Piste by ID (Invalid ID)
    @Test
    @DisplayName("Get Piste by invalid ID returns not found")
    void getById_InvalidId_ReturnsNotFound() throws Exception {
        given(pisteServices.retrievePiste(999L)).willReturn(null);

        mockMvc.perform(get("/piste/get/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Delete a Piste by ID (Valid ID)
    @Test
    @DisplayName("Delete Piste by valid ID returns no content")
    void deleteById_ValidId_ReturnsNoContent() throws Exception {
        Long validId = 1L;

        when(pisteServices.removePiste(validId)).thenReturn(true);

        mockMvc.perform(delete("/piste/delete/{id-piste}", validId))
                .andExpect(status().isNoContent());
    }

    // Delete a Piste by ID (Invalid ID)
    @Test
    @DisplayName("Delete Piste by invalid ID returns not found")
    void deleteById_InvalidId_ReturnsNotFound() throws Exception {
        long invalidId = 999L;
        doThrow(new IllegalArgumentException("Piste not found")).when(pisteServices).removePiste(invalidId);

        mockMvc.perform(delete("/piste/delete/{id-piste}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Update a Piste (Valid Data)
    @Test
    @DisplayName("Update Piste with valid data returns OK")
    void updatePiste_ValidData_ReturnsOk() throws Exception {
        Piste updatedPiste = Piste.builder()
                .numPiste(1L)
                .namePiste("Blue Ridge")
                .color(Color.BLUE)
                .length(200)
                .slope(40)
                .build();

        given(pisteServices.updatePiste(eq(1L), any(Piste.class))).willReturn(updatedPiste);

        mockMvc.perform(put("/piste/update/{idPiste}", 1L) // Specify the ID here
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPiste)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namePiste").value("Blue Ridge"))
                .andExpect(jsonPath("$.color").value(Color.BLUE.toString()));
    }

    // Update a Piste (Invalid Data)
    @Test
    @DisplayName("Update Piste with invalid data returns bad request")
    void updatePiste_InvalidData_ReturnsBadRequest() throws Exception {
        // Example of invalid data
        String invalidPisteJson = "{\"namePiste\":\"\",\"color\":\"\",\"length\":-10,\"slope\":-5}";

        // Specify an ID for the Piste being updated (it can be a valid or invalid ID since we are testing invalid data)
        Long idPiste = 1L;

        mockMvc.perform(put("/piste/update/{idPiste}", idPiste) // Include idPiste in the URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPisteJson))
                .andExpect(status().isBadRequest()); // Expect a bad request due to invalid data
    }

    // Get Piste by Name (Valid Name)
    @Test
    @DisplayName("Get Piste by Name returns OK")
    void getPisteByName_ValidName_ReturnsOk() throws Exception {
        // Given
        String namePiste = "Blue Ridge";
        Piste piste3 = Piste.builder()
                .numPiste(1L)
                .namePiste(namePiste)
                .color(Color.BLUE)
                .length(200)
                .slope(40)
                .build();

        given(pisteServices.retrievePisteByName(namePiste)).willReturn(piste3);

        // When & Then
        mockMvc.perform(get("/piste/name/{namePiste}", namePiste))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namePiste").value(namePiste))
                .andExpect(jsonPath("$.color").value(Color.BLUE.toString()));
    }

    // Get Piste by Name (Invalid Name)
    @Test
    @DisplayName("Get Piste by Name returns NOT FOUND for non-existing name")
    void getPisteByName_InvalidName_ReturnsNotFound() throws Exception {
        // Given
        String namePiste = "Nonexistent Name";
        given(pisteServices.retrievePisteByName(namePiste)).willReturn(null);

        // When & Then
        mockMvc.perform(get("/piste/name/{namePiste}", namePiste))
                .andExpect(status().isNotFound());
    }

}

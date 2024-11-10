package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RegistrationRestController.class)
@AutoConfigureMockMvc
class RegistrationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRegistrationServices registrationServices;

    @Autowired
    private ObjectMapper objectMapper;

    private Registration registration;

    @BeforeEach
    void setUp() {
        // Initialisation d'un objet Registration avant chaque test
        registration = new Registration();
    }

    @Test
    public void testAddAndAssignToSkier() throws Exception {
        Long numSkieur = 1L;

        // Mocking du comportement du service
        given(registrationServices.addRegistrationAndAssignToSkier(ArgumentMatchers.any(Registration.class), eq(numSkieur)))
                .willReturn(registration);

        // Simuler une requête PUT pour ajouter et assigner un registration à un skieur
        ResultActions response = mockMvc.perform(put("/registration/addAndAssignToSkier/{numSkieur}", numSkieur)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration)));

        // Vérifier que la réponse est correcte (statut 200 OK)
        response.andExpect(status().isOk());

        // Vérifier que le service a bien été appelé avec les bons arguments
        verify(registrationServices, times(1)).addRegistrationAndAssignToSkier(ArgumentMatchers.any(Registration.class), eq(numSkieur));
    }

    @Test
    public void testAssignToCourse() throws Exception {
        Long numRegis = 1L;
        Long numCourse = 2L;

        // Mocking du comportement du service
        given(registrationServices.assignRegistrationToCourse(numRegis, numCourse))
                .willReturn(registration);

        // Simuler une requête PUT pour assigner une registration à un cours
        ResultActions response = mockMvc.perform(put("/registration/assignToCourse/{numRegis}/{numSkieur}", numRegis, numCourse)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier que la réponse est correcte (statut 200 OK)
        response.andExpect(status().isOk());

        // Vérifier que le service a bien été appelé avec les bons arguments
        verify(registrationServices, times(1)).assignRegistrationToCourse(numRegis, numCourse);
    }

    @Test
    public void testAddAndAssignToSkierAndCourse() throws Exception {
        Long numSkieur = 1L;
        Long numCourse = 2L;

        // Mocking du comportement du service
        given(registrationServices.addRegistrationAndAssignToSkierAndCourse(ArgumentMatchers.any(Registration.class), eq(numSkieur), eq(numCourse)))
                .willReturn(registration);

        // Simuler une requête PUT pour ajouter et assigner un registration à un skieur et à un cours
        ResultActions response = mockMvc.perform(put("/registration/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}", numSkieur, numCourse)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration)));

        // Vérifier que la réponse est correcte (statut 200 OK)
        response.andExpect(status().isOk());

        // Vérifier que le service a bien été appelé avec les bons arguments
        verify(registrationServices, times(1)).addRegistrationAndAssignToSkierAndCourse(ArgumentMatchers.any(Registration.class), eq(numSkieur), eq(numCourse));
    }

    @Test
    public void testNumWeeksCourseOfInstructorBySupport() throws Exception {
        Long numInstructor = 1L;
        Support support = Support.SKI;
        List<Integer> numWeeks = Arrays.asList(1, 2, 3);

        // Mocking du comportement du service
        given(registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support))
                .willReturn(numWeeks);

        // Simuler une requête GET pour obtenir le nombre de semaines où un instructeur a donné des cours
        ResultActions response = mockMvc.perform(get("/registration/numWeeks/{numInstructor}/{support}", numInstructor, support)
                .contentType(MediaType.APPLICATION_JSON));

        // Vérifier que la réponse contient les valeurs attendues
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3));


        verify(registrationServices, times(1)).numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}

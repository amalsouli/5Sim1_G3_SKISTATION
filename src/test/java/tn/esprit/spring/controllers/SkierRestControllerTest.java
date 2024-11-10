package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkierRestController.class)
class SkierRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISkierServices skierServices;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddSkier() throws Exception {
        Skier skier = new Skier();
        skier.setSubscription(new Subscription()); // Set up your subscription as needed

        when(skierServices.addSkier(any(Skier.class))).thenReturn(skier);

        mockMvc.perform(post("/skier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscription").exists()); // Adjust based on your skier attributes
    }

    @Test
    void testGetAllSkiers() throws Exception {
        List<Skier> skiers = new ArrayList<>();
        skiers.add(new Skier()); // Add test skiers as needed

        when(skierServices.retrieveAllSkiers()).thenReturn(skiers);

        mockMvc.perform(get("/skier/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(skiers.size()));
    }

    @Test
    void testGetSkierById() throws Exception {
        Long skierId = 1L;
        Skier skier = new Skier();
        skier.setSubscription(new Subscription()); // Set up your subscription as needed

        when(skierServices.retrieveSkier(skierId)).thenReturn(skier);

        mockMvc.perform(get("/skier/get/{id-skier}", skierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscription").exists()); // Adjust based on your skier attributes
    }

    @Test
    void testDeleteSkier() throws Exception {
        Long skierId = 1L;

        // Mock the behavior of the service
        doNothing().when(skierServices).removeSkier(skierId);

        // Perform the delete request
        mockMvc.perform(delete("/skier/delete/{id-skier}", skierId))
                .andExpect(status().isOk()); // Expecting 200 OK

        // Verify that the service's remove method was called
        verify(skierServices, times(1)).removeSkier(skierId);
    }

    @Test
    void testAssignToSubscription() throws Exception {
        Long skierId = 1L;
        Long subscriptionId = 2L;
        Skier skier = new Skier();
        skier.setSubscription(new Subscription());

        when(skierServices.assignSkierToSubscription(skierId, subscriptionId)).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToSub/{numSkier}/{numSub}", skierId, subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscription").exists());
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() throws Exception {
        List<Skier> skiers = new ArrayList<>();
        skiers.add(new Skier()); // Add test skiers as needed

        when(skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL)).thenReturn(skiers);

        mockMvc.perform(get("/skier/getSkiersBySubscription")
                        .param("typeSubscription", "ANNUAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(skiers.size()));
    }
}

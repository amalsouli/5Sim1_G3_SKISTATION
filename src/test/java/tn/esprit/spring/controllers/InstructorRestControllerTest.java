package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = InstructorRestController.class)
@AutoConfigureMockMvc(addFilters = false)

 class InstructorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IInstructorServices services;

    @Autowired
    private ObjectMapper objectMapper;

    private Instructor instructor;

    private Set<Course> courses;

    @BeforeEach
    public void init(){
        courses = new HashSet<>();
        instructor = new Instructor(0L,"sami","Miled", LocalDate.of(1990, 1, 1),courses);
    }
    @Test
    void addInstructor() throws Exception{

     given(services.addInstructor(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

    }

    /*@Test
    void getAllInstrcutors() throws Exception{
        given(services.retrieveAllInstructors()).willReturn((List<Instructor>) Set.of(instructor));

        ResultActions response = mockMvc.perform(get("/instructor/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().is(200));
    }
    */


    @Test
    void updateInstructor() throws Exception {
        given(services.updateInstructor(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(put("/instructor/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instructor)))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    void getById() throws Exception {
        given(services.addInstructor(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        when(services.retrieveInstructor(0L)).thenReturn(instructor);

        ResultActions response = mockMvc.perform(get("/instructor/get/0")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().is(200));
    }
}

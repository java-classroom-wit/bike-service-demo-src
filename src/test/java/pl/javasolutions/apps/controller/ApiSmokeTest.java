package pl.javasolutions.apps.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeMechanicsEndpoint() throws Exception {
        mockMvc.perform(get("/api/mechanics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].firstName").exists());
    }

    @Test
    void shouldCreateAndUpdateRepairOrderStatus() throws Exception {
        String createPayload = """
            {
              "description": "Wymiana lancucha",
              "bicycleId": 1,
              "mechanicId": 2,
              "estimatedCost": 120.50
            }
            """;

        String createdResponse = mockMvc.perform(post("/api/repair-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.status").value("NEW"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        String id = createdResponse.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(put("/api/repair-orders/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"COMPLETED\""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.completedAt").exists());
    }
}


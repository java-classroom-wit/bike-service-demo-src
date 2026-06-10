package pl.javasolutions.apps.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.javasolutions.apps.dto.PartDTO;
import pl.javasolutions.apps.exception.ResourceNotFoundException;
import pl.javasolutions.apps.mapper.PartMapper;
import pl.javasolutions.apps.repository.model.PartEntity;
import pl.javasolutions.apps.service.PartService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartController.class)
@ActiveProfiles("test")
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartService partService;

    @MockBean
    private PartMapper partMapper;

    @Test
    void getById_existingId_returns200() throws Exception {
        PartEntity part = new PartEntity();
        part.setId(1L);
        part.setName("Łańcuch Shimano 11s");
        part.setPrice(new BigDecimal("89.99"));

        PartDTO dto = new PartDTO("Łańcuch Shimano 11s", "Shimano", new BigDecimal("89.99"));

        when(partService.findById(1L)).thenReturn(part);
        when(partMapper.toDTO(part)).thenReturn(dto);

        mockMvc.perform(get("/api/parts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Łańcuch Shimano 11s"))
                .andExpect(jsonPath("$.price").value(89.99));
    }

    @Test
    void getById_nonExistingId_returns404() throws Exception {
        when(partService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Part", 999L));

        mockMvc.perform(get("/api/parts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void create_validPart_returns201() throws Exception {
        PartDTO dto = new PartDTO("Łańcuch Shimano 11s", "Shimano", new BigDecimal("89.99"));
        PartEntity created = new PartEntity();
        created.setId(1L);
        created.setName("Łańcuch Shimano 11s");
        created.setPrice(new BigDecimal("89.99"));

        when(partService.create(any(PartDTO.class))).thenReturn(created);
        when(partMapper.toDTO(created)).thenReturn(dto);

        mockMvc.perform(post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Łańcuch Shimano 11s"));
    }

    @Test
    void create_invalidPart_returns422() throws Exception {
        PartDTO dto = new PartDTO("", "NieznanaMarka", new BigDecimal("-1"));

        mockMvc.perform(post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details").isArray());
    }
}



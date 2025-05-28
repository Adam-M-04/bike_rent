package org.example.bike_rent.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@WithMockUser(roles = "ADMIN")
class BrandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAndGetBrand() throws Exception {
        String brandJson = "{\"name\":\"TestBrand\"}";
        String postResponse = mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int brandId = objectMapper.readTree(postResponse).get("id").asInt();

        mockMvc.perform(get("/api/brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brandId)))
                .andExpect(jsonPath("$.name", is("TestBrand")));
    }

    @Test
    void updateBrand() throws Exception {
        String brandJson = "{\"name\":\"BrandToUpdate\"}";
        String postResponse = mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int brandId = objectMapper.readTree(postResponse).get("id").asInt();

        String updateJson = "{\"name\":\"UpdatedBrand\"}";
        mockMvc.perform(put("/api/brands/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedBrand")));
    }

    @Test
    void deleteBrand() throws Exception {
        String brandJson = "{\"name\":\"BrandToDelete\"}";
        String postResponse = mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        int brandId = objectMapper.readTree(postResponse).get("id").asInt();

        mockMvc.perform(delete("/api/brands/{id}", brandId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/brands/{id}", brandId))
                .andExpect(status().isNotFound());
    }
}




package dev.gabrielbarbosa.DSCatalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gabrielbarbosa.DSCatalog.dto.ProductDTO;
import dev.gabrielbarbosa.DSCatalog.entities.Category;
import dev.gabrielbarbosa.DSCatalog.entities.Product;
import dev.gabrielbarbosa.DSCatalog.services.ProductService;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.DatabaseException;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private Long existingId, nonExistingId, dependentId;


    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        Category category = new Category(1L, "Phones");
        ProductDTO productDTO = new ProductDTO(product, Set.of(category));
        PageImpl<ProductDTO> page = new PageImpl<>(List.of(productDTO));

        when(productService.findAll(any())).thenReturn(page);

        when(productService.findById(existingId)).thenReturn(productDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(productService.update(eq(existingId), any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(productService.insert(any())).thenReturn(productDTO);

        doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(eq(nonExistingId));
        doThrow(DatabaseException.class).when(productService).delete(eq(dependentId));
    }

    @Test
    public void finAllShouldReturnPage() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void updatedShouldReturnProductDTOWhenIdExists() throws Exception {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        Category category = new Category(1L, "Phones");
        ProductDTO productDTO = new ProductDTO(product, Set.of(category));

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updatedShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        Category category = new Category(1L, "Phones");
        ProductDTO productDTO = new ProductDTO(product, Set.of(category));

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception {
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        Category category = new Category(1L, "Phones");
        ProductDTO productDTO = new ProductDTO(product, Set.of(category));

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void deleteShouldReturnNoContentIdExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundDowsNotExistId() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

}

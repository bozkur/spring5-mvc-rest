package guru.springfamework.spring5mvcrest.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.spring5mvcrest.api.v1.mapper.VendorMapper;
import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;
import guru.springfamework.spring5mvcrest.domain.Vendor;
import guru.springfamework.spring5mvcrest.service.ResourceNotFoundException;
import guru.springfamework.spring5mvcrest.service.VendorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class VendorControllerTest {

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController controller;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CustomerControllerAdvice())
                .build();
    }

    @Test
    void shouldGetVendorList() throws Exception {

        VendorDto dto1 = createVendorDto("Vendor 1", 1);
        VendorDto dto2 = createVendorDto("Vendor 2", 2);

        when(vendorService.getVendors()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(MockMvcRequestBuilders.get(VendorController.URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vendors", Matchers.hasSize(2)));
    }

    @Test
    void shouldGetVendorWithId() throws Exception {
        long vendorId = 1L;
        VendorDto dto = createVendorDto("Vendor 1", vendorId);

        when(vendorService.getVendorById(vendorId)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get(VendorController.URL + "/" + vendorId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Matchers.equalTo(dto.getUrl())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldCreateNewVendor() throws Exception {
        VendorDto subject = new VendorDto();
        subject.setName("To be added");
        long id = 1L;
        VendorDto created = createVendorDto(subject.getName(), id);
        when(vendorService.createNewVendor(subject)).thenReturn(created);

        mockMvc.perform(MockMvcRequestBuilders.post(VendorController.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectAsJsonString(subject)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(created.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Matchers.equalTo(created.getUrl())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void shouldDeleteVendor() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete(VendorController.URL + "/"+ id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldPatchVendor() throws Exception {
        long id = 1L;
        VendorDto patch = new VendorDto();
        patch.setName("Patched");

        when(vendorService.patchVendor(id, patch)).thenReturn(patch);

        mockMvc.perform(MockMvcRequestBuilders.patch(VendorController.URL + "/"+ id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createObjectAsJsonString(patch)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(patch.getName())));
    }

    @Test
    void shouldUpdateVendor() throws Exception {
        long id = 1L;
        VendorDto updated = createVendorDto("Updated", id);
        when(vendorService.saveVendor(ArgumentMatchers.eq(id), ArgumentMatchers.any(VendorDto.class))).thenReturn(updated);

        mockMvc.perform(MockMvcRequestBuilders.put(VendorController.URL + "/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectAsJsonString(new VendorDto())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(updated.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Matchers.equalTo(updated.getUrl())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldSent404StatusCodeVendorToBeDisplayedCanNotBeFound() throws Exception {
        when(vendorService.getVendorById(ArgumentMatchers.anyLong())).thenThrow(new ResourceNotFoundException());
        long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(VendorController.URL + "/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectAsJsonString(new VendorDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldSent404WhenVendorToBeUpdatedCanNotBeFound() throws Exception {
        when(vendorService.saveVendor(ArgumentMatchers.anyLong(), ArgumentMatchers.any(VendorDto.class))).thenThrow(new ResourceNotFoundException());
        long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put(VendorController.URL + "/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectAsJsonString(new VendorDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldSent404WhenVendorToBePatchedVendorCanNotBeFound() throws Exception {
        when(vendorService.patchVendor(ArgumentMatchers.anyLong(), ArgumentMatchers.any(VendorDto.class))).thenThrow(new ResourceNotFoundException());
        long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch(VendorController.URL + "/"+ id)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectAsJsonString(new VendorDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private String createObjectAsJsonString(VendorDto dto) throws JsonProcessingException {
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(dto);
    }

    private VendorDto createVendorDto(String name, long id) {
        Vendor vendor = new Vendor();
        vendor.setId(id);
        vendor.setName(name);
        return VendorMapper.INSTANCE.toDto(vendor);
    }
}
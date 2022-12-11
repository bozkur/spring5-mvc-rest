package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.mapper.VendorMapper;
import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;
import guru.springfamework.spring5mvcrest.controller.v1.VendorController;
import guru.springfamework.spring5mvcrest.domain.Vendor;
import guru.springfamework.spring5mvcrest.repositories.VendorRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

class VendorServiceImplTest {

    @Mock
    private VendorRepository repository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetVendorById(){
        Vendor vendor = createVendor(1L,"deneme amaçlı dükkan");
        when(repository.findById(vendor.getId())).thenReturn(Optional.of(vendor));

        VendorDto obtainedDto = vendorService.getVendorById(vendor.getId());
        controlVendorDto(vendor, obtainedDto);
    }

    private void controlVendorDto(Vendor expected, VendorDto actual) {
        assertThat(actual.getName(), Matchers.equalTo(expected.getName()));
        assertThat(actual.getUrl(), Matchers.equalTo(VendorController.URL + "/" + expected.getId()));
    }

    private Vendor createVendor(long id, String name) {
        var vendor = new Vendor();
        vendor.setId(id);
        vendor.setName(name);
        return vendor;
    }

    @Test
    void shouldGetVendors() {
        List<Vendor> vendorList = Arrays.asList(createVendor(1, "Vendor1"), createVendor(2, "Vendor 2"));
        when(repository.findAll()).thenReturn(vendorList);

        List<VendorDto> vendorDtoList = vendorService.getVendors();

        assertThat(vendorDtoList, Matchers.hasSize(2));
        controlVendorDto(vendorList.get(0), vendorDtoList.get(0));
        controlVendorDto(vendorList.get(1), vendorDtoList.get(1));
    }

    @Test
    void shouldCreateNewVendor() {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setName("Vendor name");
        Vendor createdVendor = prepareCreatedVendor(vendorDto);
        when(repository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(createdVendor);

        VendorDto createdVendorDto = vendorService.createNewVendor(vendorDto);

        controlVendorDto(createdVendor, createdVendorDto);
    }

    private Vendor prepareCreatedVendor(VendorDto vendorDto) {
        Vendor vendor = VendorMapper.INSTANCE.toDomain(vendorDto);
        vendor.setId(11L);
        return vendor;
    }

    @Test
    void shouldSaveVendor() {
        long vendorId = 1L;
        Vendor vendor = createVendor(vendorId, "VendorName");
        String updatedName = "Vendor name updated.";

        Vendor updatedVendor = createVendor(vendorId, updatedName);
        when(repository.findById(vendorId)).thenReturn(Optional.of(vendor));
        when(repository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(updatedVendor);

        VendorDto vendorDto = new VendorDto();
        vendorDto.setName(updatedName);

        VendorDto updatedVendorDto = vendorService.saveVendor(vendorId, vendorDto);
        controlVendorDto(updatedVendor, updatedVendorDto);
    }

    @Test
    void shouldPatchVendorWhenNewNameIsSet() {
        long vendorId = 1L;
        Vendor vendor = createVendor(vendorId, "VendorName");
        String updatedName = "Vendor name updated.";

        Vendor updatedVendor = createVendor(vendorId, updatedName);
        when(repository.findById(vendorId)).thenReturn(Optional.of(vendor));
        when(repository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(updatedVendor);

        VendorDto vendorDto = new VendorDto();
        vendorDto.setName(updatedName);

        VendorDto updatedVendorDto = vendorService.patchVendor(vendorId, vendorDto);
        controlVendorDto(updatedVendor, updatedVendorDto);
    }

    @Test
    void shouldDeleteVendor() {
        long id = 1L;

        vendorService.deleteById(id);

        Mockito.verify(repository).deleteById(ArgumentMatchers.eq(id));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenVendorCanNotBeFound() {
        long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> vendorService.getVendorById(id));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAbsentVendorIsUpdated() {
        long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> vendorService.saveVendor(id, new VendorDto()));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAbsentVendorIsPatched() {
        long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> vendorService.patchVendor(id, new VendorDto()));
    }
}
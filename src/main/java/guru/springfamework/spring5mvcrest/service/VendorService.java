package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;

import java.util.List;

public interface VendorService {

    List<VendorDto> getVendors();

    VendorDto getVendorById(long id);

    VendorDto createNewVendor(VendorDto vendor);

    VendorDto saveVendor(long id, VendorDto update);

    VendorDto patchVendor(long id, VendorDto patch);

    void deleteById(long id);
}

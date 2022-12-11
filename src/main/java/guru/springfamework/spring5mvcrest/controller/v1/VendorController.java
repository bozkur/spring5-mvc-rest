package guru.springfamework.spring5mvcrest.controller.v1;

import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;
import guru.springfamework.spring5mvcrest.api.v1.model.VendorListDto;
import guru.springfamework.spring5mvcrest.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(VendorController.URL)
@RestController
public class VendorController {

    public static final String URL = "/api/v1/vendors";


    private final VendorService service;

    public VendorController(VendorService service) {
        this.service = service;
    }

    @GetMapping
    public VendorListDto getVendorList() {
        return new VendorListDto(service.getVendors());
    }

    @GetMapping("{id}")
    public VendorDto getVendorById(@PathVariable Long id) {
        return service.getVendorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDto createNewVendor(@RequestBody VendorDto dto) {
        return service.createNewVendor(dto);
    }

    @DeleteMapping("{id}")
    public void deleteVendor(@PathVariable Long id) {
        service.deleteById(id);
    }

    @PatchMapping("{id}")
    public VendorDto patchVendor(@RequestBody VendorDto patch, @PathVariable Long id) {
        return service.patchVendor(id, patch);
    }

    @PutMapping("{id}")
    public VendorDto updateVendor(@RequestBody VendorDto update, @PathVariable Long id) {
        return service.saveVendor(id, update);
    }
}

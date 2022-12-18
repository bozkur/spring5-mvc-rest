package guru.springfamework.spring5mvcrest.controller.v1;

import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;
import guru.springfamework.spring5mvcrest.api.v1.model.VendorListDto;
import guru.springfamework.spring5mvcrest.service.VendorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(description = "Meyve sağlayıcıları APİ'si")
@RequestMapping(VendorController.URL)
@RestController
public class VendorController {

    public static final String URL = "/api/v1/vendors";


    private final VendorService service;

    public VendorController(VendorService service) {
        this.service = service;
    }

    @ApiOperation(value="Sağlayıcıları listeler")
    @GetMapping
    public VendorListDto getVendorList() {
        return new VendorListDto(service.getVendors());
    }

    @ApiOperation("Eşsiz tanımlayıcı ile sağlayıcı listeler")
    @GetMapping("{id}")
    public VendorDto getVendorById(@PathVariable Long id) {
        return service.getVendorById(id);
    }

    @ApiOperation("Yeni bir sağlayıcı tanımlar")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDto createNewVendor(@RequestBody VendorDto dto) {
        return service.createNewVendor(dto);
    }

    @ApiOperation(value = "Tanımlı sağlayıcıyı siler", notes = "Silinmek istenen sağlayıcı bulunamazsa, hata atar")
    @DeleteMapping("{id}")
    public void deleteVendor(@PathVariable Long id) {
        service.deleteById(id);
    }

    @ApiOperation("Sağlayıcının belirli bilgilerini değiştirmeyi sağlar")
    @PatchMapping("{id}")
    public VendorDto patchVendor(@RequestBody VendorDto patch, @PathVariable Long id) {
        return service.patchVendor(id, patch);
    }

    @ApiOperation("Sağlayıcının tüm bilgilerinin güncellenmesini sağlar.")
    @PutMapping("{id}")
    public VendorDto updateVendor(@RequestBody VendorDto update, @PathVariable Long id) {
        return service.saveVendor(id, update);
    }
}

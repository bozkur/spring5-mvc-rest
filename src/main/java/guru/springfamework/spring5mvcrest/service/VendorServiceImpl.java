package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.mapper.VendorMapper;
import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;
import guru.springfamework.spring5mvcrest.domain.Vendor;
import guru.springfamework.spring5mvcrest.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository repository;

    private final VendorMapper mapper;

    public VendorServiceImpl(VendorRepository repository) {
        this.repository = repository;
        this.mapper = VendorMapper.INSTANCE;
    }

    @Override
    public List<VendorDto> getVendors() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public VendorDto getVendorById(long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDto createNewVendor(VendorDto vendor) {
        Vendor savedVendor = repository.save(mapper.toDomain(vendor));
        return mapper.toDto(savedVendor);
    }

    @Override
    public VendorDto saveVendor(long id, VendorDto update) {
        Optional<Vendor> vendor = repository.findById(id);
        if (vendor.isEmpty()) {
            throw new ResourceNotFoundException("Vendor with id " + id + " can not be found");
        }
        Vendor toBeSaved = mapper.toDomain(update);
        toBeSaved.setId(id);
        return mapper.toDto(repository.save(toBeSaved));
    }

    @Override
    public VendorDto patchVendor(long id, VendorDto patch) {
        Optional<Vendor> vendor = repository.findById(id);
        if (vendor.isEmpty()) {
            throw new ResourceNotFoundException("Vendor with id " + id + " can not be found");
        }
        Vendor subject = vendor.get();
        if(patchExistingVendor(subject, patch)) {
            return mapper.toDto(repository.save(subject));
        }
        throw new IllegalStateException("No state change");
    }

    private boolean patchExistingVendor(Vendor subject, VendorDto patch) {
        boolean changed = false;
        if (patch.getName() != null) {
            subject.setName(patch.getName());
            changed = true;
        }
        return changed;
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}

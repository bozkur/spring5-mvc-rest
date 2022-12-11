package guru.springfamework.spring5mvcrest.repositories;

import guru.springfamework.spring5mvcrest.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}

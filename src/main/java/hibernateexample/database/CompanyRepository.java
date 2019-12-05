package hibernateexample.database;

import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Company findCompanyById(Long l);
}

package hibernateexample.database;

import org.springframework.data.repository.CrudRepository;

public interface IndustryRepository extends CrudRepository<Industry, Long> {
    Industry findIndustryById(Long l);
}

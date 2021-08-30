package pl.futurecollars.invoicing.db.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
}

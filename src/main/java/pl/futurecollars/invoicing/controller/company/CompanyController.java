package pl.futurecollars.invoicing.controller.company;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.CompanyService;

@AllArgsConstructor
@RestController
public class CompanyController implements CompanyApi {

    private final CompanyService companyService;

    @Override
    public List<Company> getAllCompanies() {
        return companyService.getAll();
    }

    @Override
    public long addCompany(@RequestBody Company companyRq) {
        return companyService.save(companyRq);
    }

    @Override
    public ResponseEntity<Company> getSingleCompanyById(@PathVariable int id) {
        return companyService.getById(id)
                .map(company -> ResponseEntity.ok().body(company))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return companyService.delete(id)
                .map(company -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Company updatedCompany) {
        return companyService.update(id, updatedCompany)
                .map(company -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

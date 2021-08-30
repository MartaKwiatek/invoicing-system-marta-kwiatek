package pl.futurecollars.invoicing.controller.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.futurecollars.invoicing.model.Company;

@RequestMapping(value = "companies", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"company-controller"})
public interface CompanyApi {

    @ApiOperation(value = "Get list of all companies")
    @GetMapping
    List<Company> getAllCompanies();

    @ApiOperation(value = "Add new company to system")
    @PostMapping
    long addCompany(@RequestBody Company companyRq);

    @ApiOperation(value = "Get single company by id")
    @GetMapping(value = "/{id}")
    ResponseEntity<Company> getSingleCompanyById(@PathVariable int id);

    @ApiOperation(value = "Delete company with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable int id);

    @ApiOperation(value = "Update an existing company")
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable int id, @RequestBody Company updatedCompany);
}

package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.futurecollars.invoicing.model.Invoice;

@Api(tags = {"invoices"})
public interface InvoiceApi {

    @ApiOperation(value = "Get list of all invoices")
    @GetMapping(produces = {"application/json;charset=UTF-8"})
    List<Invoice> getAllInvoices();

    @ApiOperation(value = "Add new invoice to system")
    @PostMapping
    long addInvoice(@RequestBody Invoice invoiceRq);

    @ApiOperation(value = "Get single invoice by id")
    @GetMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
    ResponseEntity<Invoice> getSingleInvoiceById(@PathVariable int id);

    @ApiOperation(value = "Delete invoice with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable int id);

    @ApiOperation(value = "Update an existing invoice")
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice updatedInvoice);
}

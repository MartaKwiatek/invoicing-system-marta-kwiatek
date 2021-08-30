package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping(value = "invoices", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"invoice-controller"})
public interface InvoiceApi {

    @ApiOperation(value = "Get list of all invoices")
    @GetMapping()
    List<Invoice> getAllInvoices();

    @ApiOperation(value = "Add new invoice to system")
    @PostMapping
    long addInvoice(@RequestBody Invoice invoiceRq);

    @ApiOperation(value = "Get single invoice by id")
    @GetMapping(value = "/{id}")
    ResponseEntity<Invoice> getSingleInvoiceById(@PathVariable int id);

    @ApiOperation(value = "Delete invoice with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable int id);

    @ApiOperation(value = "Update an existing invoice")
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice updatedInvoice);
}

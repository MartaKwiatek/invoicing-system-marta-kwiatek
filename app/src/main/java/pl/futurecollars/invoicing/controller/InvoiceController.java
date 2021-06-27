package pl.futurecollars.invoicing.controller;

import java.nio.file.Path;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.config.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.FileBasedDatabase;
// import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.InvoiceService;
import pl.futurecollars.invoicing.service.JsonService;

@RestController
@RequestMapping("invoices")
public class InvoiceController {

    // Database database = new InMemoryDatabase();
    Database database = new FileBasedDatabase(Path.of(Configuration.INVOICES_PATH),
            new IdService(Path.of(Configuration.ID_PATH)), new JsonService());
    private final InvoiceService invoiceService = new InvoiceService(database);

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAll();
    }

    @PostMapping
    public int addInvoice(@RequestBody Invoice invoiceRq) {
        return invoiceService.save(invoiceRq);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getSingleInvoiceById(@PathVariable int id) {
        return invoiceService.getById(id)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return invoiceService.delete(id)
                .map(invoice -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice updatedInvoice) {
        return invoiceService.update(id, updatedInvoice)
                .map(invoice -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

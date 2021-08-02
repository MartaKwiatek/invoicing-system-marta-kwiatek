package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@RequiredArgsConstructor
public class JpaDatabase implements Database {

    private final InvoiceRepository invoiceRepository;

    @Override
    public int save(Invoice invoice) {
        return invoiceRepository.save(invoice).getId();
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return Streamable.of(invoiceRepository.findAll()).toList();
    }

    @Override
    public Optional<Invoice> update(int id, Invoice updatedInvoice) {
        Optional<Invoice> dbInvoice = getById(id);

        if (dbInvoice.isPresent()) {
            Invoice invoice = dbInvoice.get();

            updatedInvoice.setId(id);
            updatedInvoice.getBuyer().setId(invoice.getBuyer().getId());
            updatedInvoice.getSeller().setId(invoice.getSeller().getId());

            invoiceRepository.save(updatedInvoice);
        }

        return dbInvoice;
    }

    @Override
    public Optional<Invoice> delete(int id) {
        Optional<Invoice> dbInvoice = getById(id);
        dbInvoice.ifPresent(invoiceRepository::delete);
        return dbInvoice;
    }
}

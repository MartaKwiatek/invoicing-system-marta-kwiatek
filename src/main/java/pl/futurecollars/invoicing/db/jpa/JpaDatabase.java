package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@RequiredArgsConstructor
public class JpaDatabase implements Database {

    private final InvoiceRepository invoiceRepository;

    @Override
    public long save(Invoice invoice) {
        return invoiceRepository.save(invoice).getId();
    }

    @Override
    public Optional<Invoice> getById(long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return StreamSupport
                .stream(invoiceRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
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
    public Optional<Invoice> delete(long id) {
        Optional<Invoice> dbInvoice = getById(id);
        dbInvoice.ifPresent(invoiceRepository::delete);
        return dbInvoice;
    }
}

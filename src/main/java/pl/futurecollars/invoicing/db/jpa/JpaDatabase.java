package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class JpaDatabase implements Database {

    @Override
    public int save(Invoice invoice) {
        return 0;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Invoice> getAll() {
        return null;
    }

    @Override
    public Optional<Invoice> update(int id, Invoice updatedInvoice) {
        return Optional.empty();
    }

    @Override
    public Optional<Invoice> delete(int id) {
        return Optional.empty();
    }
}

package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.util.Streamable;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@AllArgsConstructor
public class MongoBasedDatabase implements Database {

    private final MongoCollection<Invoice> db;
    private final MongoIdProvider idProvider;

    @Override
    public long save(Invoice invoice) {
        invoice.setId(idProvider.getNextIdAndIncrement());
        db.insertOne(invoice);
        return invoice.getId();
    }

    @Override
    public Optional<Invoice> getById(long id) {
        return Optional.ofNullable(db.find(findById(id)).first());
    }

    private Document findById(long id) {
        return new Document("_id", id);
    }

    @Override
    public List<Invoice> getAll() {
        return Streamable.of(db.find()).toList();
    }

    @Override
    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
        updatedInvoice.setId(id);
        return Optional.ofNullable(db.findOneAndReplace(findById(id), updatedInvoice));
    }

    @Override
    public Optional<Invoice> delete(long id) {
        Invoice deletedDocument = db.findOneAndDelete(findById(id));
        return Optional.ofNullable(deletedDocument);
    }
}

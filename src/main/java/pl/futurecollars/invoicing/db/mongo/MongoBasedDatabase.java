package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.util.Streamable;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;

@AllArgsConstructor
public class MongoBasedDatabase<T extends WithId> implements Database<T> {

    private final MongoCollection<T> db;
    private final MongoIdProvider idProvider;

    @Override
    public long save(T item) {
        item.setId(idProvider.getNextIdAndIncrement());
        db.insertOne(item);
        return item.getId();
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(db.find(findById(id)).first());
    }

    private Document findById(long id) {
        return new Document("_id", id);
    }

    @Override
    public List<T> getAll() {
        return Streamable.of(db.find()).toList();
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        updatedItem.setId(id);
        return Optional.ofNullable(db.findOneAndReplace(findById(id), updatedItem));
    }

    @Override
    public Optional<T> delete(long id) {
        T deletedDocument = db.findOneAndDelete(findById(id));
        return Optional.ofNullable(deletedDocument);
    }
}

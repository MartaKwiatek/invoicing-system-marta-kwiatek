package pl.futurecollars.invoicing.db.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;

@RequiredArgsConstructor
public class JpaDatabase<T extends WithId> implements Database<T> {

    private final CrudRepository<T, Long> repository;

    @Override
    public long save(T item) {
        return repository.save(item).getId();
    }

    @Override
    public Optional<T> getById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return Streamable.of(repository.findAll()).toList();
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        Optional<T> optionalItem = getById(id);

        if (optionalItem.isPresent()) {
            repository.save(updatedItem);
        }

        return optionalItem;
    }

    @Override
    public Optional<T> delete(long id) {
        Optional<T> optionalItem = getById(id);
        optionalItem.ifPresent(repository::delete);
        return optionalItem;
    }
}

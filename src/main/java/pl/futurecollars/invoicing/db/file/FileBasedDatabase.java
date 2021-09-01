package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;
import pl.futurecollars.invoicing.service.FilesService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Log
@AllArgsConstructor
public class FileBasedDatabase<T extends WithId> implements Database<T> {

    private final Path databasePath;
    private final IdService idService;
    private final FilesService filesService;
    private final JsonService jsonService;
    private final Class<T> clazz;

    @Override
    public long save(T item) {
        try {
            long id = idService.getId();
            item.setId(id);
            idService.incrementId();
            filesService.appendLineToFile(databasePath, jsonService.objectToJsonString(item));

            return item.getId();
        } catch (IOException exception) {
            throw new RuntimeException("Saving invoice to database failed", exception);
        }
    }

    @Override
    public Optional<T> getById(long id) {
        try {
            return filesService.readAllLines(databasePath)
                    .stream()
                    .filter(line -> findById(line, id))
                    .map(line -> jsonService.stringToObject(line, clazz))
                    .findFirst();
        } catch (IOException exception) {
            throw new RuntimeException("Getting item by id failed", exception);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return filesService.readAllLines(databasePath)
                    .stream()
                    .map(line -> jsonService.stringToObject(line, clazz))
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException("Getting all items from database failed", exception);
        }
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        try {
            List<String> allItems = filesService.readAllLines(databasePath);
            var itemsWithoutItemWithGivenId = allItems
                    .stream()
                    .filter(line -> !findById(line, id))
                    .collect(Collectors.toList());

            updatedItem.setId(id);
            itemsWithoutItemWithGivenId.add(jsonService.objectToJsonString(updatedItem));

            filesService.writeLinesToFile(databasePath, itemsWithoutItemWithGivenId);

            allItems.removeAll(itemsWithoutItemWithGivenId);
            return allItems.isEmpty() ? Optional.empty()
                    : Optional.of(jsonService.stringToObject(allItems.get(0), clazz));
        } catch (IOException exception) {
            throw new RuntimeException("Updating item failed", exception);
        }
    }

    private boolean findById(String line, long id) {
        return line.contains("{\"id\":" + id + ",");
    }

    @Override
    public Optional<T> delete(long id) {
        try {
            var allItems = filesService.readAllLines(databasePath);

            var itemsExceptDeleted = allItems
                    .stream()
                    .filter(line -> !findById(line, id))
                    .collect(Collectors.toList());

            filesService.writeLinesToFile(databasePath, itemsExceptDeleted);

            allItems.removeAll(itemsExceptDeleted);

            return allItems.isEmpty() ? Optional.empty() :
                    Optional.of(jsonService.stringToObject(allItems.get(0), clazz));

        } catch (IOException exception) {
            throw new RuntimeException("Deleting item failed");
        }
    }
}

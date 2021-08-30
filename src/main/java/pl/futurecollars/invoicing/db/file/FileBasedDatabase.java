package pl.futurecollars.invoicing.db.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.WithId;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Log
public class FileBasedDatabase<T extends WithId> implements Database<T> {

    private File invoicesFile;
    private final Path invoicesPath;
    private final IdService idService;
    private final JsonService jsonService;
    private final Class<T> clazz;

    public FileBasedDatabase(Path invoicesPath, IdService idService, JsonService jsonService, Class<T> clazz) {
        this.invoicesPath = invoicesPath;
        this.idService = idService;
        this.jsonService = jsonService;
        this.clazz = clazz;

        try {
            invoicesFile = new File(String.valueOf(invoicesPath));
            invoicesFile.createNewFile();
        } catch (IOException exception) {
            log.info("Creation of invoice file failed");
        }
    }

    @Override
    public long save(T item) {
        long id = idService.getId();
        item.setId(id);
        idService.incrementId();

        try {
            Files.writeString(invoicesPath, jsonService.objectToJsonString(item), invoicesFile.exists()
                    ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
            return id;
        } catch (IOException exception) {
            throw new RuntimeException("Saving invoice to database failed", exception);
        }
    }

    @Override
    public Optional<T> getById(long id) {
        try {
            return Files.readAllLines(invoicesPath)
                    .stream()
                    .filter(line -> findById(line, id))
                    .map(line -> jsonService.stringToObject(line, clazz))
                    .findFirst();
        } catch (IOException exception) {
            throw new RuntimeException("Getting invoice by id failed", exception);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return Files.readAllLines(invoicesPath)
                    .stream()
                    .map(objectAsString -> jsonService.stringToObject(objectAsString, clazz))
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException("Getting all invoices from database failed", exception);
        }
    }

    @Override
    public Optional<T> update(long id, T updatedItem) {
        Optional<T> toUpdate = getById(id);
        updatedItem.setId(id);
        String updatedItemAsString = jsonService.objectToJsonString(updatedItem).trim();

        try {
            String itemsAsString = Files.readAllLines(invoicesPath)
                    .stream()
                    .map(invoice -> updatedInvoice(invoice, id, updatedItemAsString))
                    .collect(Collectors.joining("\n"));
            Files.writeString(invoicesPath, itemsAsString, StandardOpenOption.TRUNCATE_EXISTING);
            return toUpdate;
        } catch (IOException exception) {
            throw new RuntimeException("Updating invoice failed", exception);
        }
    }

    private boolean findById(String line, long id) {
        return line.contains("\"id\":" + id + ",\"date\"");
    }

    private String updatedInvoice(String oldInvoiceAsString, long id, String updatedInvoiceAsString) {
        return findById(oldInvoiceAsString, id) ? updatedInvoiceAsString : oldInvoiceAsString;
    }

    @Override
    public Optional<T> delete(long id) {
        Optional<T> toDelete = getById(id);
        try {
            String reducedInvoices = Files.readAllLines(invoicesPath)
                    .stream()
                    .filter(line -> !findById(line, id))
                    .collect(Collectors.joining("\n"));

            Files.writeString(invoicesPath, reducedInvoices, StandardOpenOption.TRUNCATE_EXISTING);
            return toDelete;
        } catch (IOException exception) {
            throw new RuntimeException("Deleting invoice failed");
        }
    }
}

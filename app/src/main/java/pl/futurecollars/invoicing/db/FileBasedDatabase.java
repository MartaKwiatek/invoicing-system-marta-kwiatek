package pl.futurecollars.invoicing.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.config.Configuration;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

public class FileBasedDatabase implements Database {

    private final File invoicesFile;
    private final Path invoicesPath;
    private final IdService idService;
    private final JsonService jsonService = new JsonService();

    public FileBasedDatabase() {
        invoicesFile = new File(Configuration.INVOICES_PATH);
        invoicesPath = Path.of(Configuration.INVOICES_PATH);
        idService = new IdService(Path.of(Configuration.ID_PATH));
    }

    public FileBasedDatabase(File invoicesFile, Path invoicesPath, IdService idService) {
        this.invoicesFile = invoicesFile;
        this.invoicesPath = invoicesPath;
        this.idService = idService;
    }

    @Override
    public int save(Invoice invoice) {
        int id = idService.getId();
        invoice.setId(id);
        idService.setId();

        try {
            Files.writeString(invoicesPath, jsonService.objectToString(invoice),
                    invoicesFile.exists() ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
            return id;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return -1;
    }

    @Override
    public Optional<Invoice> getById(int id) {
        List<Invoice> filteredInvoices = getAll()
                .stream()
                .filter(invoice -> invoice.getId() == id)
                .collect(Collectors.toList());

        return filteredInvoices.isEmpty() ? Optional.empty() : Optional.ofNullable(filteredInvoices.get(0));
    }

    @Override
    public List<Invoice> getAll() {
        try {
            return Files.readAllLines(invoicesPath)
                    .stream()
                    .map(jsonService::stringToObject)
                    .collect(Collectors.toList());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void update(int id, Invoice updatedInvoice) {
        if (getById(id).isEmpty()) {
            throw new IllegalArgumentException("Invoice id: " + id + " doesn't exist");
        }
        updatedInvoice.setId(id);
        String updatedInvoiceAsString = jsonService.objectToString(updatedInvoice).trim();

        try {
            String ivoicesAsString = Files.readAllLines(invoicesPath)
                    .stream()
                    .map(invoice -> updatedInvoice(invoice, id, updatedInvoiceAsString))
                    .collect(Collectors.joining("\n"));
            Files.writeString(invoicesPath, ivoicesAsString, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String updatedInvoice(String oldInvoiceAsString, int id, String updatedInvoiceAsString) {
        return oldInvoiceAsString.contains("\"id\":" + id + ",") ? updatedInvoiceAsString : oldInvoiceAsString;
    }

    @Override
    public void delete(int id) {
        try {
            String reducedInvoices = Files.readAllLines(invoicesPath)
                    .stream()
                    .filter(line -> !line.contains("\"id\":" + id + ","))
                    .collect(Collectors.joining("\n"));

            Files.writeString(invoicesPath, reducedInvoices, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

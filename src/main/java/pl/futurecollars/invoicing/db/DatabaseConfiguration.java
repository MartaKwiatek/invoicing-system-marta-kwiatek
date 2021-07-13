package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Slf4j
@Configuration
public class DatabaseConfiguration {

    @Bean
    public IdService idService(
            @Value("${invoicing-system.database.directory}") String databaseDirectory,
            @Value("${invoicing-system.database.id.file}") String idFile
    ) throws IOException {
        Path idFilePath = Files.createTempFile(databaseDirectory, idFile);
        return new IdService(idFilePath);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public Database fileBasedDatabase(
            IdService idService,
            JsonService jsonService,
            @Value("${invoicing-system.database.directory}") String databaseDirectory,
            @Value("${invoicing-system.database.invoices.file}") String invoicesFile
    ) throws IOException {
        log.info("File database has been selected");
        Path databaseFilePath = Files.createTempFile(databaseDirectory, invoicesFile);
        return new FileBasedDatabase(databaseFilePath, idService, jsonService);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    public Database inMemoryDatabase() {
        log.debug("InMemory database has been selected");
        return new InMemoryDatabase();
    }
}

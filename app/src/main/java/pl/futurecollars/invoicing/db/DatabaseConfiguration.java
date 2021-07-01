package pl.futurecollars.invoicing.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.config.PathsConfiguration;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public IdService idService() throws IOException {
        Path idFilePath = Files.createTempFile(PathsConfiguration.DATABASE_LOCATION, PathsConfiguration.ID_FILE_NAME);
        return new IdService(idFilePath);
    }

    @Bean
    public Database fileBasedDatabase(IdService idService, JsonService jsonService) throws IOException {
        Path databaseFilePath = Files.createTempFile(PathsConfiguration.DATABASE_LOCATION, PathsConfiguration.INVOICES_FILE_NAME);
        return new FileBasedDatabase(databaseFilePath, idService, jsonService);
    }
}

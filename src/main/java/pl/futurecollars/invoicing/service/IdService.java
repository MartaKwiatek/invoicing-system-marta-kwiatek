package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IdService {

    private static long id = 1;
    private final Path idFilePath;
    private final FilesService filesService;

    public IdService(Path idFilePath, FilesService filesService) {
        this.idFilePath = idFilePath;
        this.filesService = filesService;

        try {
            List<String> lines = filesService.readAllLines(idFilePath);
            if (lines.isEmpty()) {
                filesService.writeToFile(idFilePath, "1");
            } else {
                id = Integer.parseInt(lines.get(0));
            }
        } catch (IOException exception) {
            System.out.println("Creation of idFile failed");
            exception.printStackTrace();
        }
    }

    public long getId() {
        try {
            String actualId = Files.readString(idFilePath);
            return Integer.parseInt(actualId);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return id;
    }

    public void incrementId() {
        try {
            Files.writeString(idFilePath, String.valueOf(getId() + 1));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

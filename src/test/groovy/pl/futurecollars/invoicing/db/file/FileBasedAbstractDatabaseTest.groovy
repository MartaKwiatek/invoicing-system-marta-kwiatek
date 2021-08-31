package pl.futurecollars.invoicing.db.file

import java.nio.file.Files
import java.nio.file.Path
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.TestHelpers

class FileBasedAbstractDatabaseTest extends AbstractDatabaseTest {

    Path dbPath

    @Override
    Database getDatabaseInstance() {
        def idPath = File.createTempFile('ids', '.txt').toPath()

        dbPath = File.createTempFile('invoices', '.txt').toPath()

        return new FileBasedDatabase<>(dbPath, new IdService(idPath), new JsonService(), Invoice)
    }

    def "FileBasedDatabase save invoices in correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(7))

        then:
        Files.readAllLines(dbPath).size() == 1

        when:
        db.save(TestHelpers.invoice(8))

        then:
        Files.readAllLines(dbPath).size() == 2
    }
}

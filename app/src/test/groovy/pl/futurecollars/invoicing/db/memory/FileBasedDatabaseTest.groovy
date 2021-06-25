package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.FileBasedDatabase
import pl.futurecollars.invoicing.service.IdService

import java.nio.file.Files

class FileBasedDatabaseTest extends DatabaseTest {

    def dbPath

    @Override
    Database getDatabaseInstance() {
        def idPath = File.createTempFile('ids', '.txt').toPath()
        dbPath = File.createTempFile('invoices', '.txt').toPath()

        return new FileBasedDatabase(new File(dbPath as String), dbPath, new IdService(idPath))
    }

    def "FileBasedDatabase save invoices in correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(7))

        then:
        1 == Files.readAllLines(dbPath).size()

        when:
        db.save(TestHelpers.invoice(8))

        then:
        2 == Files.readAllLines(dbPath).size()
    }
}

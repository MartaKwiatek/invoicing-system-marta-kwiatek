package pl.futurecollars.invoicing.db

class InMemoryDatabaseTest extends DatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDatabase()
    }
}

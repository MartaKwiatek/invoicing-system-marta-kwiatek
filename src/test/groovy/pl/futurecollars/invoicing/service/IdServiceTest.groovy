package pl.futurecollars.invoicing.service

import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

class IdServiceTest extends Specification{

    private Path nextIdPath = File.createTempFile('nextId', 'txt').toPath()

    def "if file was empty, next id starts from 1"() {
        given:
        IdService idService = new IdService(nextIdPath, new FilesService())

        expect:
        ['1'] == Files.readAllLines(nextIdPath)

        and:
        idService.incrementId()
        ['2'] == Files.readAllLines(nextIdPath)

        and:
        idService.incrementId()
        ['3'] == Files.readAllLines(nextIdPath)
    }

    def "if file was not empty, next id starts from last number"() {
        given:
        Files.writeString(nextIdPath, "666")
        IdService idService = new IdService(nextIdPath, new FilesService())

        expect:
        ['666'] == Files.readAllLines(nextIdPath)

        and:
        idService.incrementId()
        ['667'] == Files.readAllLines(nextIdPath)

        and:
        idService.incrementId()
        ['668'] == Files.readAllLines(nextIdPath)

        and:
        idService.incrementId()
        ['669'] == Files.readAllLines(nextIdPath)
    }
    }

package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

    private final Database database;

    public BigDecimal income(String taxIdNumber) {
        return database.visit(sellerPredicate(taxIdNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal costs(String taxIdNumber) {
        return database.visit(buyerPredicate(taxIdNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal incomingVat(String taxIdNumber) {
        return database.visit(sellerPredicate(taxIdNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal outgoingVat(String taxIdNumber) {
        return database.visit(buyerPredicate(taxIdNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal earnings(String taxIdNumber) {
        return income(taxIdNumber).subtract(costs(taxIdNumber));
    }

    public BigDecimal vatToPay(String taxIdNumber) {
        return incomingVat(taxIdNumber).subtract(outgoingVat(taxIdNumber));
    }

    public TaxResult calculateTaxes(String taxIdNumber) {
        return TaxResult.builder()
                .income(income(taxIdNumber))
                .costs(costs(taxIdNumber))
                .incomingVat(incomingVat(taxIdNumber))
                .outgoingVat(outgoingVat(taxIdNumber))
                .earnings(earnings(taxIdNumber))
                .vatToPay(vatToPay(taxIdNumber))
                .build();
    }

    private Predicate<Invoice> buyerPredicate(String taxIdNumber) {
        return invoice -> invoice.getBuyer().getTaxIdNumber().equals(taxIdNumber);
    }

    private Predicate<Invoice> sellerPredicate(String taxIdNumber) {
        return invoice -> invoice.getSeller().getTaxIdNumber().equals(taxIdNumber);
    }
}

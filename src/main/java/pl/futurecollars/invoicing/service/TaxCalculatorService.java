package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

    private final Database<Invoice> database;

    public BigDecimal income(String taxIdNumber) {
        return visit(sellerPredicate(taxIdNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal costs(String taxIdNumber) {
        return visit(buyerPredicate(taxIdNumber), this::getCostsIncludingPersonalExpense);
    }

    private BigDecimal getCostsIncludingPersonalExpense(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getPrice()
                .add(invoiceEntry.getVatValue())
                .subtract(getVatValueIncludingPersonalExpense(invoiceEntry));
    }

    public BigDecimal earnings(String taxIdNumber) {
        return income(taxIdNumber).subtract(costs(taxIdNumber));
    }

    public BigDecimal incomingVat(String taxIdNumber) {
        return visit(sellerPredicate(taxIdNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal outgoingVat(Company company) {
        return visit(buyerPredicate(company.getTaxIdNumber()), this::getVatValueIncludingPersonalExpense);
    }

    private BigDecimal getVatValueIncludingPersonalExpense(InvoiceEntry invoiceEntry) {
        return Optional.ofNullable(invoiceEntry.getCarExpense())
                .map(Car::isPersonalUse)
                .map(personalUse -> personalUse ? BigDecimal.valueOf(0.5) : BigDecimal.ONE)
                .map(multiplayer -> invoiceEntry.getVatValue().multiply(multiplayer))
                .map(value -> value.setScale(2, RoundingMode.DOWN))
                .orElse(invoiceEntry.getVatValue());
    }

    public BigDecimal vatToPay(Company company) {
        return incomingVat(company.getTaxIdNumber()).subtract(outgoingVat(company));
    }

    public TaxResult calculateTaxes(Company company) {
        BigDecimal earningsMinusPensionInsurance = earnings(company.getTaxIdNumber()).subtract(company.getPensionInsurance());
        BigDecimal taxCalculationBase = earningsMinusPensionInsurance.setScale(0, RoundingMode.HALF_DOWN);
        BigDecimal incomeTax = taxCalculationBase.multiply(BigDecimal.valueOf(19, 2));
        BigDecimal healthInsuranceAmountToReduceTax = company.getHealthInsurance().multiply(BigDecimal.valueOf(775))
                .divide(BigDecimal.valueOf(900), RoundingMode.HALF_UP);
        BigDecimal incomeTaxMinusHealthInsurance = incomeTax.subtract(healthInsuranceAmountToReduceTax);
        BigDecimal finalIncomeTax = incomeTaxMinusHealthInsurance.setScale(0, RoundingMode.DOWN);

        return TaxResult.builder()
                .income(income(company.getTaxIdNumber()))
                .costs(costs(company.getTaxIdNumber()))
                .earnings(earnings(company.getTaxIdNumber()))
                .incomingVat(incomingVat(company.getTaxIdNumber()))
                .outgoingVat(outgoingVat(company))
                .vatToPay(vatToPay(company))
                .pensionInsurance(company.getPensionInsurance())
                .healthInsurance(healthInsuranceAmountToReduceTax)
                .earningsMinusPensionInsurance(earningsMinusPensionInsurance)
                .taxCalculationBase(taxCalculationBase)
                .incomeTax(incomeTax)
                .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance)
                .finalIncomeTax(finalIncomeTax)
                .build();
    }

    private Predicate<Invoice> buyerPredicate(String taxIdNumber) {
        return invoice -> invoice.getBuyer().getTaxIdNumber().equals(taxIdNumber);
    }

    private Predicate<Invoice> sellerPredicate(String taxIdNumber) {
        return invoice -> invoice.getSeller().getTaxIdNumber().equals(taxIdNumber);
    }

    private BigDecimal visit(Predicate<Invoice> filterRules, Function<InvoiceEntry, BigDecimal> amountToSelect) {
        return database.getAll().stream()
                .filter(filterRules)
                .flatMap(invoice -> invoice.getEntries().stream())
                .map(amountToSelect)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

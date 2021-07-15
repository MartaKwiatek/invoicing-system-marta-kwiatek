package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.service.TaxCalculatorService;
import pl.futurecollars.invoicing.service.TaxResult;

@RestController
@AllArgsConstructor
@RequestMapping("taxes")
public class TaxCalculatorController implements TaxCalculatorApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public TaxResult getCalculatedTaxes(String taxIdNumber) {
        return taxCalculatorService.calculateTaxes(taxIdNumber);
    }
}

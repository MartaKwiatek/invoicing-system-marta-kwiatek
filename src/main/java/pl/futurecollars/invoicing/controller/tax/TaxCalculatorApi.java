package pl.futurecollars.invoicing.controller.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.TaxResult;

@CrossOrigin
@RequestMapping(value = "taxes", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"tax-controller"})
public interface TaxCalculatorApi {

    @ApiOperation(value =
            "Get calculated taxes (income, costs, incoming VAT, outgoing VAT, earnings and VAT to pay) for specified tax Identification Number")
    @PostMapping
    TaxResult getCalculatedTaxes(@RequestBody Company company);
}

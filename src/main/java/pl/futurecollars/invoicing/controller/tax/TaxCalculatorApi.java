package pl.futurecollars.invoicing.controller.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.TaxResult;

@Api(tags = {"taxes"})
public interface TaxCalculatorApi {

    @ApiOperation(value =
            "Get calculated taxes (income, costs, incoming VAT, outgoing VAT, earnings and VAT to pay) for specified tax Identification Number")
    @PostMapping(value = "/{taxIdNumber}", produces = {"application/json;charset=UTF-8"})
    TaxResult getCalculatedTaxes(@RequestBody Company company);
}

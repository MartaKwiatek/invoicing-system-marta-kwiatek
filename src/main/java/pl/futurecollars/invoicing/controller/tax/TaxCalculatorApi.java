package pl.futurecollars.invoicing.controller.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.futurecollars.invoicing.service.TaxResult;

@Api(tags = {"taxes"})
public interface TaxCalculatorApi {

    @ApiOperation(value =
            "Get calculated taxes (income, costs, incoming VAT, outgoing VAT, earnings and VAT to pay) for specified tax Identification Number")
    @GetMapping(value = "/{taxIdNumber}", produces = {"application/json;charset=UTF-8"})
    TaxResult getCalculatedTaxes(@PathVariable String taxIdNumber);
}

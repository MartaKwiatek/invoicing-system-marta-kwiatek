package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @ApiModelProperty(value = "Tax identification number", required = true, example = "666-666-69-69")
    private String taxIdNumber;

    @ApiModelProperty(value = "Company address", required = true, example = "ul. Żwirki i Wigury 80, 87-100 Toruń")
    private String address;

    @ApiModelProperty(value = "Name of Company", required = true, example = "Radio Szatan. Głos szatana w Twoim domu xD")
    private String name;

    @Builder.Default
    @ApiModelProperty(value = "Health insurance amount", required = true, example = "1567.99")
    private BigDecimal healthInsurance = BigDecimal.ZERO;

    @Builder.Default
    @ApiModelProperty(value = "Pension insurance amount", required = true, example = "662.01")
    private BigDecimal pensionInsurance = BigDecimal.ZERO;
}

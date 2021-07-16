package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
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
}

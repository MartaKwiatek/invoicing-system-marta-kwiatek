package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class Car {

    @ApiModelProperty(value = "True if the car is also used for private purposes", required = true, example = "true")
    private final boolean isIncludingPrivateExpense;

    @ApiModelProperty(value = "Car registration number", required = true, example = "DW583XX")
    private final String registration;
}

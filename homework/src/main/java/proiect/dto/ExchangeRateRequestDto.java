package proiect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateRequestDto {
    private Long sourceCurrencyId;
    private Long destinationCurrencyId;
    private double conversionRate;
}
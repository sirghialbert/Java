package proiect.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import proiect.dto.CurrencyResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponseDto {
    private Long id;
    private CurrencyResponseDto sourceCurrency;
    private CurrencyResponseDto destinationCurrency;
    private double conversionRate;
}
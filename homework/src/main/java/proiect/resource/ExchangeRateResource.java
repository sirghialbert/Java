package proiect.resource;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.dto.ExchangeRateRequestDto;
import proiect.dto.ExchangeRateResponseDto;
import proiect.service.ExchangeRateService;

import java.util.List;

@RestController
@RequestMapping("/exchange_rates")
@AllArgsConstructor
public class ExchangeRateResource {
    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<List<ExchangeRateResponseDto>> getExchangeRates() {
        List<ExchangeRateResponseDto> exchangeRateResponseDtos = exchangeRateService.getAllExchangeRates();

        return ResponseEntity.ok(exchangeRateResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ExchangeRateResponseDto> createExchangeRate(@RequestBody @Valid ExchangeRateRequestDto
                                                                                  exchangeRateRequestDto) {
        return ResponseEntity.ok(exchangeRateService.createExchangeRate(exchangeRateRequestDto));
    }
}

package proiect.resource;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.dto.CurrencyRequestDto;
import proiect.dto.CurrencyResponseDto;
import proiect.service.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@AllArgsConstructor
public class CurrencyResource {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyResponseDto>> getCurrencies() {
        List<CurrencyResponseDto> currencyResponseDtos = currencyService.getAllCurrencies();

        return ResponseEntity.ok(currencyResponseDtos);
    }

    @PostMapping
    public ResponseEntity<CurrencyResponseDto> createCurrency(@RequestBody @Valid CurrencyRequestDto currencyRequestDto) {
        return ResponseEntity.ok(currencyService.createCurrency(currencyRequestDto));
    }
}

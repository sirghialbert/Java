package proiect.service;

import org.springframework.stereotype.Service;
import proiect.dto.CurrencyRequestDto;
import proiect.dto.CurrencyResponseDto;
import proiect.exception.CurrencyNotFoundException;
import proiect.model.Currency;
import proiect.repository.CurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public CurrencyResponseDto createCurrency(CurrencyRequestDto currencyRequestDto) {
        Currency currency = new Currency(currencyRequestDto.getType());
        currencyRepository.save(currency);

        return new CurrencyResponseDto(currency.getId(),currency.getType());
    }

    public List<CurrencyResponseDto> getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();

        return currencies.stream()
                .map(currency -> new CurrencyResponseDto(currency.getId(), currency.getType()))
                .collect(Collectors.toList());
    }

    public CurrencyResponseDto getCurrencyById(Long currencyId) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(CurrencyNotFoundException::new);

        return new CurrencyResponseDto(currency.getId(), currency.getType());
    }
}

package proiect.service;

import org.springframework.stereotype.Service;
import proiect.dto.CurrencyResponseDto;
import proiect.dto.ExchangeRateRequestDto;
import proiect.dto.ExchangeRateResponseDto;
import proiect.exception.CurrencyNotFoundException;
import proiect.exception.ExchangeRateNotFoundException;
import proiect.model.Currency;
import proiect.model.ExchangeRate;
import proiect.repository.CurrencyRepository;
import proiect.repository.ExchangeRateRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrencyRepository currencyRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
    }

    public ExchangeRateResponseDto createExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        Currency sourceCurrency = currencyRepository.findById(exchangeRateRequestDto.getSourceCurrencyId())
                .orElseThrow(CurrencyNotFoundException::new);

        Currency destinationCurrency = currencyRepository.findById(exchangeRateRequestDto.getDestinationCurrencyId())
                .orElseThrow(CurrencyNotFoundException::new);

        ExchangeRate exchangeRate = new ExchangeRate(sourceCurrency, destinationCurrency, exchangeRateRequestDto.getConversionRate());
        exchangeRateRepository.save(exchangeRate);

        return new ExchangeRateResponseDto(exchangeRate.getId(),
                new CurrencyResponseDto(sourceCurrency.getId(), sourceCurrency.getType()),
                new CurrencyResponseDto(destinationCurrency.getId(),destinationCurrency.getType()),
                exchangeRate.getConversionRate());
    }

    public List<ExchangeRateResponseDto> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

        return exchangeRates.stream()
                .map(exchangeRate -> new ExchangeRateResponseDto(exchangeRate.getId(),
                        new CurrencyResponseDto(exchangeRate.getSourceCurrency().getId(),
                               exchangeRate.getSourceCurrency().getType()),
                        new CurrencyResponseDto(exchangeRate.getDestinationCurrency().getId(),
                                exchangeRate.getDestinationCurrency().getType()
                                ),
                        exchangeRate.getConversionRate()))
                .collect(Collectors.toList());
    }
    public ExchangeRateResponseDto getExchangeRateByCurrencies(Long sourceCurrencyId, Long destinationCurrencyId) {
        Currency sourceCurrency = currencyRepository.findById(sourceCurrencyId)
                .orElseThrow(CurrencyNotFoundException::new);

        Currency destinationCurrency = currencyRepository.findById(destinationCurrencyId)
                .orElseThrow(CurrencyNotFoundException::new);

        ExchangeRate exchangeRate = exchangeRateRepository.findByCurrencies(sourceCurrency, destinationCurrency)
                .orElseThrow(ExchangeRateNotFoundException::new);

        return new ExchangeRateResponseDto(exchangeRate.getId(),
                new CurrencyResponseDto(sourceCurrency.getId(), sourceCurrency.getType()),
                new CurrencyResponseDto(destinationCurrency.getId(), destinationCurrency.getType()),
                exchangeRate.getConversionRate());
    }

    public ExchangeRateResponseDto getExchangeRateById(Long exchangeRateId) {
        ExchangeRate exchangeRate = exchangeRateRepository.findById(exchangeRateId)
                .orElseThrow(ExchangeRateNotFoundException::new);

        return new ExchangeRateResponseDto(exchangeRate.getId(),
                new CurrencyResponseDto(exchangeRate.getSourceCurrency().getId(),
                        exchangeRate.getSourceCurrency().getType()),
                new CurrencyResponseDto(exchangeRate.getDestinationCurrency().getId(),
                        exchangeRate.getDestinationCurrency().getType()),
                exchangeRate.getConversionRate());
    }
}

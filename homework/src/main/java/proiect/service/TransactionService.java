package proiect.service;

import org.springframework.stereotype.Service;
import proiect.dto.ExchangeRateResponseDto;
import proiect.dto.TransactionRequestDto;
import proiect.dto.TransactionResponseDto;
import proiect.exception.AccountNotFoundException;
import proiect.exception.CurrencyNotFoundException;
import proiect.model.Account;
import proiect.model.Currency;
import proiect.model.ExchangeRate;
import proiect.model.Transaction;
import proiect.repository.AccountRepository;
import proiect.repository.CurrencyRepository;
import proiect.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    public TransactionService(TransactionRepository transactionRepository, ExchangeRateService exchangeRateService, AccountRepository accountRepository, CurrencyRepository currencyRepository) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateService = exchangeRateService;
        this.accountRepository = accountRepository;
        this.currencyRepository = currencyRepository;
    }

    public TransactionResponseDto saveTransaction(TransactionRequestDto transactionRequestDto) {
        Account sourceAccount = accountRepository.findById(transactionRequestDto.getSourceAccountId())
                .orElseThrow(AccountNotFoundException::new);
        Account destAccount = accountRepository.findById(transactionRequestDto.getDestinationAccountId())
                .orElseThrow(AccountNotFoundException::new);

        Currency sourceCurrency = sourceAccount.getCurrency();
        Currency destCurrency = destAccount.getCurrency();

        ExchangeRateResponseDto exchangeRate = exchangeRateService.getExchangeRateByCurrencies(sourceCurrency.getId(), destCurrency.getId());

        double convertedAmount = transactionRequestDto.getAmount() * exchangeRate.getConversionRate();

        Transaction transaction = transactionRepository.save(new Transaction(
                sourceAccount,
                destAccount,
                mapExchangeRateResponseDtoToExchangeRate(exchangeRate),
                convertedAmount,
                transactionRequestDto.getDate(),
                transactionRequestDto.getType()
        ));

        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setSourceAccountId(transactionRequestDto.getSourceAccountId());
        responseDto.setDestinationAccountId(transactionRequestDto.getDestinationAccountId());
        responseDto.setAmount(convertedAmount);
        responseDto.setExchangeRateId(exchangeRate.getId());
        responseDto.setDate(transactionRequestDto.getDate());
        responseDto.setType(transactionRequestDto.getType());

        return responseDto;
    }

    public ExchangeRate mapExchangeRateResponseDtoToExchangeRate(ExchangeRateResponseDto responseDto) {
        Currency sourceCurrency = currencyRepository.findById(responseDto.getSourceCurrency().getId())
                .orElseThrow(CurrencyNotFoundException::new);
        Currency destinationCurrency = currencyRepository.findById(responseDto.getDestinationCurrency().getId())
                .orElseThrow(CurrencyNotFoundException::new);

        return new ExchangeRate(sourceCurrency, destinationCurrency, responseDto.getConversionRate());
    }

    private TransactionResponseDto mapTransactionToResponseDto(Transaction transaction) {
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setId(transaction.getId());
        responseDto.setSourceAccountId(transaction.getSourceAccount().getId());
        responseDto.setDestinationAccountId(transaction.getDestinationAccount().getId());
        responseDto.setAmount(transaction.getSum());

        return responseDto;
    }

    public List<TransactionResponseDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::mapTransactionToResponseDto)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> getTransactionsBySourceAccount(Account sourceAccount) {
        List<Transaction> transactions = transactionRepository.findAllBySourceAccount(sourceAccount);
        return transactions.stream()
                .map(this::mapTransactionToResponseDto)
                .collect(Collectors.toList());
    }
}
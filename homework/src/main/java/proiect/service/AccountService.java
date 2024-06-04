package proiect.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import proiect.dto.AccountRequestDto;
import proiect.dto.AccountResponseDto;
import proiect.dto.CurrencyResponseDto;
import proiect.dto.UserResponseDto;
import proiect.exception.AccountNotFoundException;
import proiect.exception.CurrencyNotFoundException;
import proiect.exception.UserNotFoundException;
import proiect.model.Account;
import proiect.model.Currency;
import proiect.model.User;
import proiect.repository.AccountRepository;
import proiect.repository.CurrencyRepository;
import proiect.repository.UserRepository;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public AccountResponseDto saveAccount(AccountRequestDto accountRequestDto, String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Currency currency = currencyRepository.findById(accountRequestDto.getCurrencyId()).orElseThrow(CurrencyNotFoundException::new);

        Account account = new Account(user, currency, accountRequestDto.getBalance());
        Account savedAccount = accountRepository.save(account);

        return mapToAccountResponseDto(savedAccount);
    }

    public AccountResponseDto updateAccountBalance(Long accountId, double newBalance) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        account.setBalance(newBalance);
        Account updatedAccount = accountRepository.save(account);
        return mapToAccountResponseDto(updatedAccount);
    }

    private AccountResponseDto mapToAccountResponseDto(Account account) {
        return new AccountResponseDto(account.getId(), account.getUser().getUsername(), account.getCurrency().getType(),
                account.getBalance());
    }

    private CurrencyResponseDto mapToCurrencyResponseDto(Currency currency) {
        return new CurrencyResponseDto(currency.getId(), currency.getType());
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }

    public AccountResponseDto getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        return mapToAccountResponseDto(account);
    }

    public CurrencyResponseDto getAccountCurrency(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        Currency currency = account.getCurrency();
        return mapToCurrencyResponseDto(currency);
    }

    public UserResponseDto getAccountUser(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        User user = account.getUser();
        return mapToUserResponseDto(user);
    }
}

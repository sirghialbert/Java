package proiect.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proiect.dto.*;
import proiect.exception.AccountNotFoundException;
import proiect.exception.UnauthorizedException;
import proiect.exception.UserNotFoundException;
import proiect.model.Account;
import proiect.model.Transaction;
import proiect.model.User;
import proiect.repository.AccountRepository;
import proiect.repository.UserRepository;
import proiect.security.JwtProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private static final long TTL = 5;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto saveUser(UserRequestDto userDto) {
        final String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User(userDto.getUsername(), encodedPassword, userDto.getAuthorizationRoles());
        User savedUser = userRepository.save(user);
        return mapToUserResponseDto(savedUser);
    }

    public UserResponseDto getUserByName(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        return mapToUserResponseDto(user);
    }

    public List<AccountResponseDto> getAccounts(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Set<Account> accounts = user.getAccounts();
        return accounts.stream()
                .map(this::mapToAccountResponseDto)
                .collect(Collectors.toList());
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }

    private AccountResponseDto mapToAccountResponseDto(Account account) {
        return new AccountResponseDto(account.getId(), account.getUser().getUsername(),account.getCurrency().getType(),
                account.getBalance());
    }

    public AccountResponseDto addAccountToUser(String username, Long accountId) {
        User user = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

        user.addAccount(account);
        userRepository.save(user);

        return mapToAccountResponseDto(account);
    }

    public String authenticate(UserRequestDto userDto) {
        User user = userRepository.findUserByUsername(userDto.getUsername()).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }

        Set<String> roles = Arrays.stream(user.getAuthorizationRoles().split(",")).collect(Collectors.toSet());

        return jwtProvider.generateToken(userDto.getUsername(), TTL, roles);
    }

    public AccountResponseDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
        return mapToAccountResponseDto(account);
    }

    private TransactionResponseDto mapToTransactionResponseDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getSourceAccount().getId(),
                transaction.getDestinationAccount().getId(),
                transaction.getExchangeRate().getId(),
                transaction.getSum(),
                transaction.getDate(),
                transaction.getType()
        );
    }
}



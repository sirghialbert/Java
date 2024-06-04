package proiect.resource;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.dto.*;
import proiect.exception.AccountNotFoundException;
import proiect.model.Account;
import proiect.repository.AccountRepository;
import proiect.repository.UserRepository;
import proiect.security.JwtProvider;
import proiect.service.TransactionService;
import proiect.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserResource {
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> userResponseDtos = userService.getAllUsers();

        return ResponseEntity.ok(userResponseDtos);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userDto) {
        if (userRepository.findUserByUsername(userDto.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userService.saveUser(userDto));
    }

    @PostMapping(value = "/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationDto> createJwt(@RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok().body(new AuthenticationDto(userService.authenticate(userDto)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUser() {
        String username = JwtProvider.getLoggedUserName();

        return ResponseEntity.ok(userService.getUserByName(username));
    }

    @GetMapping("/me/accounts")
    public ResponseEntity<List<AccountResponseDto>> getUserAccounts() {
        String username = JwtProvider.getLoggedUserName();

        return ResponseEntity.ok(userService.getAccounts(username));
    }

    @GetMapping("/me/accounts/{id}")
    public ResponseEntity<AccountResponseDto> getUserAccountById(@PathVariable Long id) {
        String username = JwtProvider.getLoggedUserName();
        Account account = accountRepository.findById(id).orElseThrow(AccountNotFoundException::new);

        if (account.getUser().getUsername().equals(username)) {
            return ResponseEntity.ok(userService.getAccountById(id));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/me/accounts/{id}/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

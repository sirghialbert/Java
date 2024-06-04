package proiect.resource;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.dto.TransactionRequestDto;
import proiect.dto.TransactionResponseDto;
import proiect.exception.AccountNotFoundException;
import proiect.exception.UserNotFoundException;
import proiect.model.Account;
import proiect.model.User;
import proiect.repository.AccountRepository;
import proiect.repository.UserRepository;
import proiect.security.JwtProvider;
import proiect.service.AccountService;
import proiect.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionResource {
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        List<TransactionResponseDto> transactionResponseDtos = transactionService.getAllTransactions();

        return ResponseEntity.ok(transactionResponseDtos);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> saveTransaction(@RequestBody @Valid TransactionRequestDto transactionDto) {
        String username = JwtProvider.getLoggedUserName();
        Account sourceAccount = accountRepository.findById(transactionDto.getSourceAccountId()).orElseThrow(AccountNotFoundException::new);

        if (transactionDto.getAmount() < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (sourceAccount.getUser().getUsername().equals(username)) {
            TransactionResponseDto transactionResponseDto = transactionService.saveTransaction(transactionDto);

            double sourceNewBalance = accountRepository.findById(transactionDto.getSourceAccountId())
                    .orElseThrow(AccountNotFoundException::new).getBalance()
                    - transactionResponseDto.getAmount();

            accountService.updateAccountBalance(transactionDto.getSourceAccountId(), sourceNewBalance);

            double destinationNewBalance = accountRepository.findById(transactionDto.getDestinationAccountId())
                    .orElseThrow(AccountNotFoundException::new).getBalance()
                    + transactionResponseDto.getAmount();

            accountService.updateAccountBalance(transactionDto.getDestinationAccountId(), destinationNewBalance);

            return ResponseEntity.ok(transactionResponseDto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

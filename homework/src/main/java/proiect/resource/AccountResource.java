package proiect.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proiect.dto.AccountRequestDto;
import proiect.dto.AccountResponseDto;
import proiect.exception.UserNotFoundException;
import proiect.model.User;
import proiect.repository.UserRepository;
import proiect.security.JwtProvider;
import proiect.service.AccountService;
import proiect.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountResource {
    private final AccountService accountService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AccountResponseDto> addNewAccountToUser(@RequestBody @Valid AccountRequestDto accountDto) {
        String username = JwtProvider.getLoggedUserName();
        User loggedUser = userRepository.findUserByUsername(username).orElseThrow(UserNotFoundException::new);

        if (loggedUser.getId().equals(accountDto.getUserId())) {
            AccountResponseDto accountResponseDto = accountService.saveAccount(accountDto, username);

            return ResponseEntity.ok(userService.addAccountToUser(username, accountResponseDto.getId()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

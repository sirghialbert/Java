package proiect;

import proiect.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Main implements CommandLineRunner {
public AccountRepository accountRepository;
public CurrencyRepository currencyRepository;
public ExchangeRateRepository exchangeRateRepository;
public TransactionRepository transactionRepository;
public UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }

    @Override
    public void run(String... args) {



    }


}
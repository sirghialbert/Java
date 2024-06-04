package proiect.repository;

import proiect.model.Account;
import proiect.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import proiect.model.User;

import java.util.List;

@Component
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySourceAccount(Account sourceAccount);
}

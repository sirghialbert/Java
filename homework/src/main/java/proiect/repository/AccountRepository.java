package proiect.repository;

import proiect.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import proiect.model.User;

import java.util.List;

@Component
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
}

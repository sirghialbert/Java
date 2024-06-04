package proiect.repository;

import proiect.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}

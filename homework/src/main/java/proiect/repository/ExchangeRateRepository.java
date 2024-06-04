package proiect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import proiect.model.Currency;
import proiect.model.ExchangeRate;

import java.util.Optional;

@Component
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT er FROM ExchangeRate er WHERE er.sourceCurrency = :sourceCurrency AND er.destinationCurrency = :destinationCurrency")
    Optional<ExchangeRate> findByCurrencies(@Param("sourceCurrency") Currency sourceCurrency, @Param("destinationCurrency") Currency destinationCurrency);
}


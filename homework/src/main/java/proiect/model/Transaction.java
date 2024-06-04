package proiect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "exchange_rate_id", nullable = false)
    private ExchangeRate exchangeRate;

    @Column(nullable = false)
    private double sum;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String type;

    public Transaction(Account sourceAccount, Account destinationAccount, ExchangeRate exchangeRate,
                       double sum, String date, String type) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.exchangeRate = exchangeRate;
        this.sum = sum;
        this.date = date;
        this.type = type;
    }
}
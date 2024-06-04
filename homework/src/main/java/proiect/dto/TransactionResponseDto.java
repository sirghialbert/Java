package proiect.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private Long exchangeRateId;
    private double amount;
    private String date;
    private String type;
}
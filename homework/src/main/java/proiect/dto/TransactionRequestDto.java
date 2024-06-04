package proiect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {
    private Long sourceAccountId;
    private Long destinationAccountId;
    private double amount;
    private String type;
    private String date;
}

package proiect.dto;

import lombok.AllArgsConstructor;
import proiect.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String username;
    private String currencyType;
    private double balance;
}
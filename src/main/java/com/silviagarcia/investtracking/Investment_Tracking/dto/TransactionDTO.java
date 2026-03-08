package com.silviagarcia.investtracking.Investment_Tracking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private Double stocks;
    private Double purchasePrice;
    private Double invEur;
    private LocalDateTime purchaseDate;
}
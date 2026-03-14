package com.silviagarcia.investtracking.Investment_Tracking.dto;

import lombok.Data;
import java.util.List;

@Data
public class ItemDTO {
    private Long id;
    private String name;
    private CategoryDTO category;
    private List<TransactionDTO> transactions;
    private Double currentPrice;
}
package com.silviagarcia.investtracking.Investment_Tracking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private Double stocks;

    @Column()
    private Double purchasePrice;

    @Column()
    private Double invEur;

    @Column()
    private LocalDateTime purchaseDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
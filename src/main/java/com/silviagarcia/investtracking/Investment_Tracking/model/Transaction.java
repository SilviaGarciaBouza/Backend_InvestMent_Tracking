package com.silviagarcia.investtracking.Investment_Tracking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
/**
        * Entidad que registra las operaciones financieras (transacciones) de cada ítem.
 */
@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cantidad de acciones o participaciones. Obligatorio. */
    @Column(nullable = false)
    private Double stocks;

    /** Precio de compra unitario. Obligatorio. */
    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    /** Inversión total en Euros. Obligatorio. */
    @Column(name = "inv_eur", nullable = false)
    private Double invEur;

    /** Fecha y hora de la transacción. Mapeado a 'datetime' en MariaDB. */
    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    /** Referencia al ítem padre. No se incluye en el JSON principal para evitar bucles. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonBackReference
    @ToString.Exclude
    private Item item;

}
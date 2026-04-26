package com.silviagarcia.investtracking.investment_tracking.model;

import jakarta.persistence.*;
import lombok.Data;
/**
 * Entidad que representa las categorías de inversión.
 */
@Entity
@Table(name = "categories")
@Data
public class Category {
    /** Identificador único auto-incremental. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la categoría. */
    @Column(nullable = false, length = 255)
    private String name;
}
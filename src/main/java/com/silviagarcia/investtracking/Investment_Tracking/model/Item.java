package com.silviagarcia.investtracking.Investment_Tracking.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
/**
 * Entidad que representa un activo o ítem de inversión.
 * Relaciona usuarios con categorías y posee una lista de transacciones.
 */
@Entity
@Table(name = "items")
@Data
public class Item {
    /** ID del ítem con estrategia Identity (auto_increment). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** Nombre del ítem. Requerido (NOT NULL). */
    @Column(nullable = false, length = 255)
    private String name;
    /** Usuario propietario del ítem. Relación Muchos a Uno. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /** Categoría asociada al ítem. Relación Muchos a Uno. */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    /** * Lista de transacciones asociadas.
     * JsonManagedReference para evitar recursividad infinita en JSON.
     */
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<Transaction> transactions;
}
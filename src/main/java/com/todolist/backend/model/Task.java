package com.todolist.backend.model;
import jakarta.persistence.*;      //  Importaciones necesarias para JPA
import lombok.AllArgsConstructor;  // Lombok = Código automático / menos repetición
import lombok.Data;
import lombok.NoArgsConstructor;   // ----------------------------------------------

@Entity // Entidad -> -Tabla en BD
@Data   // genera -> Getters + Setters + toString + equals + hashCode
@NoArgsConstructor  // Constructor vacío (requerido por JPA)
@AllArgsConstructor // Constructor con todos los atributos
public class Task {

    @Id // marca -> Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT = ID automático generado por BD
    private Long id;

    // ------------------ atributos ------------------
    private String title;
    private String description;
    private Boolean completed;
}

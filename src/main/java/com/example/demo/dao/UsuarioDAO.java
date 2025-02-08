package com.example.demo.dao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="usuario")
public class UsuarioDAO {


    public UsuarioDAO(String nombre, String email, String telefono, LocalDate fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "usuario")
    private List<PrestamoDAO> prestamos;


    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(unique= true, nullable = false, length = 13)
    private String telefono;

    @Column(name ="fecha_publicacion",nullable = false)
    private LocalDate fechaRegistro;
}

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
@Table(name="libro")
public class LibroDAO {


    public LibroDAO(String titulo, String autor, String isbn, LocalDate fechaPublicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.fechaPublicacion = fechaPublicacion;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "libro")
    private List<PrestamoDAO> prestamos;


    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(unique= true, nullable = false, length = 13)
    private String isbn;

    @Column(name ="fecha_publicacion",nullable = false)
    private LocalDate fechaPublicacion;
}

package com.example.demo.dao;

import com.example.demo.dao.LibroDAO;
import com.example.demo.dao.UsuarioDAO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
@Data
@NoArgsConstructor
public class PrestamoDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private LibroDAO libro;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioDAO usuario;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public PrestamoDAO(LibroDAO libro, UsuarioDAO usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

}

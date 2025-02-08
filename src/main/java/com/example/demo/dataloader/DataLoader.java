package com.example.demo.dataloader;

import com.example.demo.dao.LibroDAO;
import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dao.PrestamoDAO;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.persistence.Column;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public DataLoader(LibroRepository libroRepository, UsuarioRepository usuarioRepository, PrestamoRepository prestamoRepository){
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public void run(String... args) throws Exception {


    List<LibroDAO> libros = new ArrayList<>();
    for (int i = 0; i<3;i++) {

        Long id = (long) 1;
        String titulo = "titulo"+i;
        String autor = "autor"+i;
        String isbn = "isbn"+i;
        LocalDate fechaPublicacion = LocalDate.now();
        LibroDAO libro = new LibroDAO(
                titulo,
                autor,
                isbn,
                fechaPublicacion
        );
        libros.add(libroRepository.save(libro));
        libroRepository.save(libro);
    }

    List<UsuarioDAO> usuarios = new ArrayList<>();
    for (int i = 0; i<3;i++) {

        Long id = (long) 1;
        String nombre = "nombre"+i;
        String email = "email"+i;
        String telefono = "34"+i;
        LocalDate fechaRegistro = LocalDate.now();
        UsuarioDAO usuario = new UsuarioDAO(
                nombre,
                email,
                telefono,
                fechaRegistro
        );
        usuarios.add(usuarioRepository.save(usuario));
        usuarioRepository.save(usuario);
    }
        for (int i = 0; i < 3; i++) {
            LibroDAO libro = libros.get(i);
            UsuarioDAO usuario = usuarios.get(i);
            LocalDate fechaPrestamo = LocalDate.now();
            LocalDate fechaDevolucion = fechaPrestamo.plusDays(10);

            PrestamoDAO prestamo = new PrestamoDAO(libro, usuario, fechaPrestamo, fechaDevolucion);
            prestamoRepository.save(prestamo);
        }
    }
}

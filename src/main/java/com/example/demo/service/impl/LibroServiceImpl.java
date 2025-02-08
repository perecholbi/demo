package com.example.demo.service.impl;

import com.example.demo.controller.LibroController;
import com.example.demo.dao.LibroDAO;
import com.example.demo.dto.LibroDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.mapper.LibroMapper;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.service.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class LibroServiceImpl implements LibroService {


    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    private static final LibroMapper libroMapper = LibroMapper.INSTANCE;

    @Override
    public List<LibroDTO> obtenerLibros(){

        log.info("LibroController obtenerLibros...");

        List<LibroDAO> result = libroRepository.findAll();

        List<LibroDTO> resultado = libroMapper.libroDaosToLibroDtos(result);

        log.info("LibroController obtenerLibros se han obtenido {} resultaods", resultado.size());

        return resultado;
    }

    @Override
    public LibroDTO obtenerLibroId(Long id){
        log.info("LibroController obtenerLibro...");
        Optional<LibroDAO> result = libroRepository.findById(id);

        if (result.isPresent()) {
            return libroMapper.libroDaoToLibroDto(result.get());
        } else {
            return null;
        }
    }

    @Override
    public LibroDTO guardarLibro(LibroDTO libroDTO) {
        log.info("Creando un nuevo libro...");
        if (libroDTO == null || libroDTO.getTitulo() == null || libroDTO.getAutor() == null || libroDTO.getIsbn() == null || libroDTO.getFechaPublicacion() == null) {
            log.error("Datos invalidos");
            throw new DatosInvalidosException("Los datos del libro son inválidos o están incompletos");
        }
        LibroDAO libroDAO = libroMapper.libroDtoToLibroDao(libroDTO);
        libroDAO = libroRepository.save(libroDAO);
        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroDAO);
        log.info("Libro creado con éxito: {}", resultado);

        return resultado;
    }

    @Override
    public LibroDTO modificarLibro(Long id, LibroDTO libroDTO) {
        log.info("Modificando libro con ID: {}", id);
        if (libroDTO == null || libroDTO.getTitulo() == null || libroDTO.getAutor() == null || libroDTO.getFechaPublicacion() == null) {
            log.error("Datos invalidos");
            throw new DatosInvalidosException("Los datos del libro son inválidos o están incompletos");
        }

        Optional<LibroDAO> result = libroRepository.findById(id);

        if (result.isPresent()) {
            LibroDAO libroExistente = result.get();

            libroExistente.setAutor(libroDTO.getAutor());
            libroExistente.setTitulo(libroDTO.getTitulo());
            libroExistente.setIsbn(libroDTO.getIsbn());
            libroExistente.setFechaPublicacion(libroDTO.getFechaPublicacion());

            libroExistente = libroRepository.save(libroExistente);

            LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroExistente);

            log.info("Libro modificado con éxito: {}", resultado);

            return resultado;
        } else {
            log.info("No se encontró el libro con ID: {}", id);
            throw new RecursoNoEncontradoException("Los datos del libro son inválidos o están incompletos");
        }
    }

    @Override
    public LibroDTO modificarParcialmenteLibro(Long id, LibroDTO libroDTO) {
        log.info("Modificando parcialmente libro con ID: {}", id);

        Optional<LibroDAO> result = libroRepository.findById(id);
        if (!result.isPresent()) {
            log.info("No se encontró el libro con ID: {}", id);
            return null;
        }


        LibroDAO libroExistente = result.get();
        if (libroDTO.getAutor() != null) {
            libroExistente.setAutor(libroDTO.getAutor());
        }
        if (libroDTO.getTitulo() != null) {
            libroExistente.setTitulo(libroDTO.getTitulo());
        }
        if (libroDTO.getIsbn() != null) {
            libroExistente.setIsbn(libroDTO.getIsbn());
        }
        if (libroDTO.getFechaPublicacion() != null) {
            libroExistente.setFechaPublicacion(libroDTO.getFechaPublicacion());
        }

        libroExistente = libroRepository.save(libroExistente);
        LibroDTO resultado = libroMapper.libroDaoToLibroDto(libroExistente);

        log.info("Libro modificado parcialmente con éxito: {}", resultado);
        return resultado;
    }


    @Override
    public boolean eliminarLibro(Long id) {
        log.info("Eliminando libro con ID: {}", id);
        Optional<LibroDAO> result = libroRepository.findById(id);
        if (prestamoRepository.existsByLibroId(id)) {
            throw new IllegalStateException("No se puede eliminar el libro porque tiene préstamos asociados.");
        }
        if (result.isPresent()) {
            libroRepository.deleteById(id);
            log.info("Libro con ID: {} eliminado con éxito", id);
            return true;
        } else {
            log.error("No se encontró el libro con ID: {}", id);
            throw new RecursoNoEncontradoException("No se encontró el libro con ID: " + id);
        }
    }
}


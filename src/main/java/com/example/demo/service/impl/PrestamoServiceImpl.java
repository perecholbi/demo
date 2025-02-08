package com.example.demo.service.impl;

import com.example.demo.dao.LibroDAO;
import com.example.demo.dao.PrestamoDAO;
import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dto.PrestamoDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.mapper.PrestamoMapper;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.PrestamoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final PrestamoMapper prestamoMapper = PrestamoMapper.INSTANCE;

    @Override
    public List<PrestamoDTO> obtenerPrestamos(){

        log.info("PrestamoController obtenerPrestamos...");

        List<PrestamoDAO> result = prestamoRepository.findAll();

        List<PrestamoDTO> resultado = prestamoMapper.prestamosDaoToPrestamosDto(result);

        log.info("PrestamoController obtenerPrestamos se han obtenido {} resultaods", resultado.size());

        return resultado;
    }

    @Override
    public PrestamoDTO obtenerPrestamoId(Long id){
        log.info("PrestamoController obtenerPrestamo...");
        Optional<PrestamoDAO> result = prestamoRepository.findById(id);

        if (result.isPresent()) {
            return prestamoMapper.prestamoDaoToPrestamoDto(result.get());
        } else {
            return null;
        }
    }


    @Override
    public PrestamoDTO modificarPrestamo(Long id, PrestamoDTO prestamoDTO) {
        log.info("Modificando préstamo con ID: {}", id);
        if (prestamoDTO == null || prestamoDTO.getLibroId() == null || prestamoDTO.getUsuarioId() == null || prestamoDTO.getFechaPrestamo() == null || prestamoDTO.getFechaDevolucion() == null) {
            log.error("Datos invalidos");
            throw new DatosInvalidosException("Los datos del usuario son inválidos o están incompletos");
        }

        Optional<PrestamoDAO> result = prestamoRepository.findById(id);

        if (result.isPresent()) {
            PrestamoDAO prestamoExistente = result.get();

            LibroDAO libro = libroRepository.findById(prestamoDTO.getLibroId())
                                            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

            UsuarioDAO usuario = usuarioRepository.findById(prestamoDTO.getUsuarioId())
                                                  .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            prestamoExistente.setLibro(libro);
            prestamoExistente.setUsuario(usuario);
            prestamoExistente.setFechaPrestamo(prestamoDTO.getFechaPrestamo());
            prestamoExistente.setFechaDevolucion(prestamoDTO.getFechaDevolucion());

            prestamoExistente = prestamoRepository.save(prestamoExistente);

            PrestamoDTO resultado = prestamoMapper.prestamoDaoToPrestamoDto(prestamoExistente);

            log.info("Préstamo modificado con éxito: {}", resultado);
            return resultado;
        } else {
            log.warn("No se encontró el préstamo con ID: {}", id);
            throw new RecursoNoEncontradoException("No existe el prestamo");
        }
    }

    @Override
    public PrestamoDTO guardarPrestamo(PrestamoDTO prestamoDTO) {
        log.info("Creando un nuevo usuario...");
        if (prestamoDTO == null || prestamoDTO.getLibroId() == null || prestamoDTO.getUsuarioId() == null || prestamoDTO.getFechaPrestamo() == null || prestamoDTO.getFechaDevolucion() == null) {
            log.error("Datos invalidos");
            throw new DatosInvalidosException("Los datos del usuario son inválidos o están incompletos");
        }
        PrestamoDAO prestamoDAO = prestamoMapper.prestamoDtoToPrestamoDao(prestamoDTO);
        prestamoDAO = prestamoRepository.save(prestamoDAO);
        PrestamoDTO resultado = prestamoMapper.prestamoDaoToPrestamoDto(prestamoDAO);
        log.info("Prestamo creado con éxito: {}", resultado);

        return resultado;
    }

    @Override
    public PrestamoDTO modificarParcialmentePrestamo(Long id, PrestamoDTO prestamoDTO) {
        log.info("Actualizando parcialmente préstamo con ID: {}", id);

        Optional<PrestamoDAO> result = prestamoRepository.findById(id);

        if (result.isPresent()) {
            PrestamoDAO prestamoExistente = result.get();

            if (prestamoDTO.getLibroId() != null) {
                LibroDAO libro = libroRepository.findById(prestamoDTO.getLibroId())
                                                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
                prestamoExistente.setLibro(libro);
            }

            if (prestamoDTO.getUsuarioId() != null) {
                UsuarioDAO usuario = usuarioRepository.findById(prestamoDTO.getUsuarioId())
                                                      .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                prestamoExistente.setUsuario(usuario);
            }

            if (prestamoDTO.getFechaPrestamo() != null) {
                prestamoExistente.setFechaPrestamo(prestamoDTO.getFechaPrestamo());
            }

            if (prestamoDTO.getFechaDevolucion() != null) {
                prestamoExistente.setFechaDevolucion(prestamoDTO.getFechaDevolucion());
            }

            prestamoExistente = prestamoRepository.save(prestamoExistente);

            PrestamoDTO resultado = prestamoMapper.prestamoDaoToPrestamoDto(prestamoExistente);

            log.info("Préstamo actualizado parcialmente con éxito: {}", resultado);
            return resultado;
        } else {
            log.warn("No se encontró el préstamo con ID: {}", id);
            //throw new RuntimeException("Préstamo no encontrado");
            return null;
        }
    }

    @Override
    public boolean eliminarPrestamo(Long id) {
        log.info("Eliminando prestamo con ID: {}", id);
        Optional<PrestamoDAO> result = prestamoRepository.findById(id);
        if (result.isPresent()) {
            prestamoRepository.deleteById(id);
            log.info("Prestamo con ID: {} eliminado con éxito", id);
            return true;
        } else {
            log.error("No se encontró el prestamo con ID: {}", id);
            throw new RecursoNoEncontradoException("No se encontró el usuario con ID: " + id);

        }
    }
}

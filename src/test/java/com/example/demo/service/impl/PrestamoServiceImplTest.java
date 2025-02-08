package com.example.demo.service.impl;

import com.example.demo.dao.LibroDAO;
import com.example.demo.dao.PrestamoDAO;
import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dto.PrestamoDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PrestamoServiceImplTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PrestamoServiceImpl prestamoServiceImpl;

    @BeforeEach
    void setUp(){ MockitoAnnotations.openMocks(this);}

    @Test
    void obtenerPrestamos() {

        List<PrestamoDAO> prestamos = new ArrayList<>();

        LibroDAO libroMock = new LibroDAO("titulo1", "Autor1", "ISBN1", LocalDate.now());
        UsuarioDAO usuarioMock = new UsuarioDAO("nombre1", "email1", "telefono1", LocalDate.now());
        PrestamoDAO prestamoMock = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());
        PrestamoDAO prestamoMock2 = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());
        prestamos.add(prestamoMock);
        prestamos.add(prestamoMock2);




        when(prestamoRepository.findAll()).thenReturn(prestamos);

        List<PrestamoDTO> resultado = prestamoServiceImpl.obtenerPrestamos();

        assertEquals(2, resultado.size());

    }

    @Test
    void obtenerPrestamoId_existente() {
        LibroDAO libroMock = new LibroDAO("titulo1", "Autor1", "ISBN1", LocalDate.now());
        libroMock.setId(10L);
        UsuarioDAO usuarioMock = new UsuarioDAO("nombre1", "email1", "telefono1", LocalDate.now());
        PrestamoDAO prestamoMock = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());

        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamoMock));

        PrestamoDTO resultado = prestamoServiceImpl.obtenerPrestamoId(1L);

        assertNotNull(resultado);
        assertEquals(10, resultado.getLibroId());
    }

    @Test
    void obtenerPrestamoId_noExistente() {
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        PrestamoDTO resultado = prestamoServiceImpl.obtenerPrestamoId(99L);

        assertNull(resultado);
    }

    @Test
    void guardarPrestamo() {
        LibroDAO libroMock = new LibroDAO("titulo1", "Autor1", "ISBN1", LocalDate.now());
        libroMock.setId(10L);
        UsuarioDAO usuarioMock = new UsuarioDAO("nombre1", "email1", "telefono1", LocalDate.now());
        PrestamoDAO prestamoMock = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoMock);

        PrestamoDTO prestamoDTO = new PrestamoDTO(null, 2L, 3L, LocalDate.now(), LocalDate.now());
        PrestamoDTO resultado = prestamoServiceImpl.guardarPrestamo(prestamoDTO);

        assertNotNull(resultado);
        assertEquals(10, resultado.getLibroId());
    }

    @Test
    void modificarPrestamo_existente() {
        LibroDAO libroMock = new LibroDAO("titulo1", "Autor1", "ISBN1", LocalDate.now());
        libroMock.setId(10L);
        UsuarioDAO usuarioMock = new UsuarioDAO("nombre1", "email1", "telefono1", LocalDate.now());
        PrestamoDAO prestamoMock = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamoMock));
        when(libroRepository.findById(10L)).thenReturn(Optional.of(libroMock));
        when(usuarioRepository.findById(13L)).thenReturn(Optional.of(usuarioMock));
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoMock);

        PrestamoDTO prestamoDTO = new PrestamoDTO(null, 10L, 13L, LocalDate.of(2000,3,11), LocalDate.of(2010,7,11));
        PrestamoDTO resultado = prestamoServiceImpl.modificarPrestamo(1L, prestamoDTO);

        assertNotNull(resultado);
        assertEquals(10, resultado.getLibroId());
    }


    @Test
    void modificarPrestamo_noExistente() {
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DatosInvalidosException.class, () ->
                prestamoServiceImpl.modificarPrestamo(99L, new PrestamoDTO())
        );
    }



    @Test
    void modificarParcialmentePrestamo_existente() {
        LibroDAO libroMock = new LibroDAO("titulo1", "Autor1", "ISBN1", LocalDate.now());
        libroMock.setId(10L);
        LibroDAO nuevoLibroMock = new LibroDAO("nuevoTitulo", "nuevoAutor", "nuevoISBN", LocalDate.now());
        nuevoLibroMock.setId(11L);
        UsuarioDAO usuarioMock = new UsuarioDAO("nombre1", "email1", "telefono1", LocalDate.now());
        PrestamoDAO prestamoMock = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());
        prestamoMock.setId(1L);

        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamoMock));
        when(libroRepository.findById(11L)).thenReturn(Optional.of(nuevoLibroMock));
        when(prestamoRepository.save(any(PrestamoDAO.class))).thenReturn(prestamoMock);

        PrestamoDTO prestamoParcial = new PrestamoDTO();
        prestamoParcial.setLibroId(11L);

        PrestamoDTO resultado = prestamoServiceImpl.modificarParcialmentePrestamo(1L, prestamoParcial);

        assertNotNull(resultado);
        assertEquals(11L, resultado.getLibroId());
    }

    @Test
    void modificarParcialmentePrestamo_noExistente() {
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        PrestamoDTO resultado = prestamoServiceImpl.modificarParcialmentePrestamo(99L, new PrestamoDTO());

        assertNull(resultado);
    }

    @Test
    void eliminarPrestamo_existente() {
        LibroDAO libroMock = new LibroDAO("titulo1", "Autor1", "ISBN1", LocalDate.now());
        libroMock.setId(10L);
        UsuarioDAO usuarioMock = new UsuarioDAO("nombre1", "email1", "telefono1", LocalDate.now());
        PrestamoDAO prestamoMock = new PrestamoDAO(libroMock, usuarioMock, LocalDate.now(), LocalDate.now());
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamoMock));

        boolean resultado = prestamoServiceImpl.eliminarPrestamo(1L);

        assertTrue(resultado);
        verify(prestamoRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarPrestamo_noExistente() {
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> prestamoServiceImpl.eliminarPrestamo(99L));
    }

}


package com.example.demo.service.impl;

import com.example.demo.dao.LibroDAO;
import com.example.demo.dto.LibroDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.LibroRepository;
import com.example.demo.repository.PrestamoRepository;
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

class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private LibroServiceImpl libroServiceImpl;

    @BeforeEach
    void setUp(){ MockitoAnnotations.openMocks(this);}

    @Test
    void obtenerLibros() {

        List<LibroDAO> libros = new ArrayList<>();

        for (int i = 0; i<2;i++) {

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
            libros.add(libro);
        }

        when(libroRepository.findAll()).thenReturn(libros);

        List<LibroDTO> resultado = libroServiceImpl.obtenerLibros();

        assertEquals(2, resultado.size());

    }

    @Test
    void obtenerLibroId_existente() {
        LibroDAO libroMock = new LibroDAO("Libro1", "Autor1", "ISBN1", LocalDate.now());
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroMock));

        LibroDTO resultado = libroServiceImpl.obtenerLibroId(1L);

        assertNotNull(resultado);
        assertEquals("Libro1", resultado.getTitulo());
    }

    @Test
    void obtenerLibroId_noExistente() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        LibroDTO resultado = libroServiceImpl.obtenerLibroId(99L);

        assertNull(resultado);
    }

    @Test
    void guardarLibro() {
        LibroDAO libroMock = new LibroDAO("Nuevo Libro", "Nuevo Autor", "isbn123", LocalDate.now());
        when(libroRepository.save(any(LibroDAO.class))).thenReturn(libroMock);

        LibroDTO libroDTO = new LibroDTO(null, "Nuevo Libro", "Nuevo Autor", "isbn123", LocalDate.now());
        LibroDTO resultado = libroServiceImpl.guardarLibro(libroDTO);

        assertNotNull(resultado);
        assertEquals("Nuevo Libro", resultado.getTitulo());
    }

    @Test
    void modificarLibro_existente() {
        LibroDAO libroMock = new LibroDAO("Libro1", "Autor1", "ISBN1", LocalDate.now());
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroMock));
        when(libroRepository.save(any(LibroDAO.class))).thenReturn(libroMock);

        LibroDTO libroDTO = new LibroDTO(null, "Libro Modificado", "Autor Modificado", "isbn999", LocalDate.now());
        LibroDTO resultado = libroServiceImpl.modificarLibro(1L, libroDTO);

        assertNotNull(resultado);
        assertEquals("Libro Modificado", resultado.getTitulo());
    }


    @Test
    void modificarLibro_noExistente() {
    when(libroRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(DatosInvalidosException.class, () ->
            libroServiceImpl.modificarLibro(99L, new LibroDTO())
            );
    }

    @Test
    void modificarLibro_datosInvalidos() {
        LibroDTO libroDTOInvalido = new LibroDTO();
        libroDTOInvalido.setAutor("Autor de prueba");

        assertThrows(DatosInvalidosException.class, () -> {
            libroServiceImpl.modificarLibro(1L, libroDTOInvalido);
        });
    }


    @Test
    void modificarParcialmenteLibro_existente() {
        LibroDAO libroMock = new LibroDAO("Libro1", "Autor1", "ISBN1", LocalDate.now());
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroMock));
        when(libroRepository.save(any(LibroDAO.class))).thenReturn(libroMock);

        LibroDTO libroParcial = new LibroDTO();
        libroParcial.setTitulo("Nuevo Título");

        LibroDTO resultado = libroServiceImpl.modificarParcialmenteLibro(1L, libroParcial);

        assertNotNull(resultado);
        assertEquals("Nuevo Título", resultado.getTitulo());
    }

    @Test
    void modificarParcialmenteLibro_noExistente() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        LibroDTO resultado = libroServiceImpl.modificarParcialmenteLibro(99L, new LibroDTO());

        assertNull(resultado);
    }

    @Test
    void eliminarLibro_existente() {
        LibroDAO libroMock = new LibroDAO("Libro1", "Autor1", "ISBN1", LocalDate.now());
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroMock));

        boolean resultado = libroServiceImpl.eliminarLibro(1L);

        assertTrue(resultado);
        verify(libroRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarLibro_noExistente() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> libroServiceImpl.eliminarLibro(99L));
    }

}


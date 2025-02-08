package com.example.demo.controller;

import com.example.demo.dto.LibroDTO;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.service.LibroService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private LibroService libroService;

    @Test
    void obtenerLibros() throws Exception {

        List<LibroDTO> librosMock = new ArrayList<>();

        LocalDate fecha1 = LocalDate.now();
        LocalDate fecha2 = LocalDate.now();


        LibroDTO libro1 = new LibroDTO(1L, "Libro1", "autor1", "isbn1", fecha1);
        LibroDTO libro2 =new LibroDTO(1L, "Libro2", "autor2","isbn2",fecha2);

        librosMock.add(libro1);
        librosMock.add(libro2);

        when(libroService.obtenerLibros()).thenReturn(librosMock);

        mockMvc.perform(MockMvcRequestBuilders.get("/libros"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", Matchers.is(librosMock.size())))
               .andExpect(jsonPath("$[0].titulo", Matchers.is("Libro1")))
               .andExpect(jsonPath("$[1].autor", Matchers.is("autor2")));

        verify(libroService, times (1)).obtenerLibros();

    }

    @Test
    void obtenerLibroId_existente() throws Exception {
        LibroDTO libroMock = new LibroDTO(1L, "Libro1", "autor1", "isbn1", LocalDate.now());

        when(libroService.obtenerLibroId(1L)).thenReturn(libroMock);

        mockMvc.perform(get("/libros/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titulo", Matchers.is("Libro1")))
               .andExpect(jsonPath("$.autor", Matchers.is("autor1")));

        verify(libroService, times(1)).obtenerLibroId(1L);
    }

    @Test
    void obtenerLibroId_noExistente() throws Exception {
        when(libroService.obtenerLibroId(99L)).thenReturn(null);

        mockMvc.perform(get("/libros/99"))
               .andExpect(status().isNotFound());

        verify(libroService, times(1)).obtenerLibroId(99L);
    }

    @Test
    void guardarLibro() throws Exception {
        LibroDTO libroMock = new LibroDTO(1L, "Nuevo Libro", "Nuevo Autor", "isbn123", LocalDate.now());

        when(libroService.guardarLibro(any(LibroDTO.class))).thenReturn(libroMock);

        mockMvc.perform(post("/libros")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(libroMock)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titulo", Matchers.is("Nuevo Libro")))
               .andExpect(jsonPath("$.autor", Matchers.is("Nuevo Autor")));

        verify(libroService, times(1)).guardarLibro(any(LibroDTO.class));
    }

    @Test
    void modificarLibro_existente() throws Exception {
        LibroDTO libroActualizado = new LibroDTO(1L, "Libro Modificado", "Autor Modificado", "isbn999", LocalDate.now());

        when(libroService.modificarLibro(eq(1L), any(LibroDTO.class))).thenReturn(libroActualizado);

        mockMvc.perform(put("/libros/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(libroActualizado)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titulo", Matchers.is("Libro Modificado")))
               .andExpect(jsonPath("$.autor", Matchers.is("Autor Modificado")));

        verify(libroService, times(1)).modificarLibro(eq(1L), any(LibroDTO.class));
    }


    @Test
    void modificarParcialmenteLibro_existente() throws Exception {
        LibroDTO libroParcial = new LibroDTO();
        libroParcial.setTitulo("Nuevo Título");

        LibroDTO libroActualizado = new LibroDTO(1L, "Nuevo Título", "Autor1", "isbn1", LocalDate.now());

        when(libroService.modificarParcialmenteLibro(eq(1L), any(LibroDTO.class))).thenReturn(libroActualizado);

        mockMvc.perform(patch("/libros/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(libroParcial)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.titulo", Matchers.is("Nuevo Título")));

        verify(libroService, times(1)).modificarParcialmenteLibro(eq(1L), any(LibroDTO.class));
    }

    @Test
    void modificarParcialmenteLibro_noExistente() throws Exception {
        when(libroService.modificarParcialmenteLibro(eq(99L), any(LibroDTO.class))).thenReturn(null);

        mockMvc.perform(patch("/libros/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new LibroDTO())))
               .andExpect(status().isNotFound());

        verify(libroService, times(1)).modificarParcialmenteLibro(eq(99L), any(LibroDTO.class));
    }

    @Test
    void eliminarLibro_existente() throws Exception {
        when(libroService.eliminarLibro(1L)).thenReturn(true);

        mockMvc.perform(delete("/libros/1"))
               .andExpect(status().isNoContent());

        verify(libroService, times(1)).eliminarLibro(1L);
    }

}

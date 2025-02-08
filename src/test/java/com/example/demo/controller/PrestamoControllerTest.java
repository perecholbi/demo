package com.example.demo.controller;

//@ExtendWith(MockitoExtension.class)

import com.example.demo.dto.LibroDTO;
import com.example.demo.dto.PrestamoDTO;
import com.example.demo.dto.PrestamoDTO;
import com.example.demo.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(PrestamoController.class)
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrestamoService prestamoService;

    @Test
    
    void obtenerPrestamos() throws Exception {

        List<PrestamoDTO> prestamosMock = new ArrayList<>();

        LocalDate fecha1 = LocalDate.now();
        LocalDate fecha2 = LocalDate.now();


        PrestamoDTO prestamo1 =new  PrestamoDTO(1L, 2L, 3L, LocalDate.now(), LocalDate.now());
        PrestamoDTO prestamo2 =new PrestamoDTO(1L, 2L, 3L, LocalDate.now(), LocalDate.now());

        prestamosMock.add(prestamo1);
        prestamosMock.add(prestamo2);

        when(prestamoService.obtenerPrestamos()).thenReturn(prestamosMock);

        mockMvc.perform(MockMvcRequestBuilders.get("/prestamos"))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(prestamosMock.size())))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].libroId", Matchers.is(2)))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].usuarioId", Matchers.is(3)));

        verify(prestamoService, times (1)).obtenerPrestamos();

    }

    @Test
    void obtenerPrestmoId_existente() throws Exception {
        PrestamoDTO prestamoMock = new PrestamoDTO(1L, 2L, 3L, LocalDate.now(), LocalDate.now());

        when(prestamoService.obtenerPrestamoId(1L)).thenReturn(prestamoMock);

        mockMvc.perform(get("/prestamos/1"))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.libroId", Matchers.is(2)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.usuarioId", Matchers.is(3)));

        verify(prestamoService, times(1)).obtenerPrestamoId(1L);
    }

    @Test
    void obtenerPrestamoId_noExistente() throws Exception {
        when(prestamoService.obtenerPrestamoId(99L)).thenReturn(null);

        mockMvc.perform(get("/prestamos/99"))
               .andExpect(status().isNotFound());

        verify(prestamoService, times(1)).obtenerPrestamoId(99L);
    }

    @Test
    void guardarPrestamo() throws Exception {
        PrestamoDTO prestamoMock = new PrestamoDTO(1L, 2L, 3L, LocalDate.now(), LocalDate.now());

        when(prestamoService.guardarPrestamo(any(PrestamoDTO.class))).thenReturn(prestamoMock);

        mockMvc.perform(MockMvcRequestBuilders.post("/prestamos")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(prestamoMock)))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.libroId", Matchers.is(2)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.usuarioId", Matchers.is(3)));

        verify(prestamoService, times(1)).guardarPrestamo(any(PrestamoDTO.class));
    }

    @Test
    void modificarPrestamo_existente() throws Exception {
        PrestamoDTO prestamoActualizado = new PrestamoDTO(1L, 2L, 3L, LocalDate.now(), LocalDate.now());

        when(prestamoService.modificarPrestamo(eq(1L), any(PrestamoDTO.class))).thenReturn(prestamoActualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/prestamos/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(prestamoActualizado)))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.libroId", Matchers.is(2)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.usuarioId", Matchers.is(3)));

        verify(prestamoService, times(1)).modificarPrestamo(eq(1L), any(PrestamoDTO.class));
    }


    @Test
    void modificarParcialmentePrestamo_existente() throws Exception {
        PrestamoDTO prestamoParcial = new PrestamoDTO();
        prestamoParcial.setLibroId(2L);

        PrestamoDTO prestamoActualizado = new PrestamoDTO(1L, 2L, 3L, LocalDate.now(), LocalDate.now());

        when(prestamoService.modificarParcialmentePrestamo(eq(1L), any(PrestamoDTO.class))).thenReturn(prestamoActualizado);

        mockMvc.perform(MockMvcRequestBuilders.patch("/prestamos/1")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(prestamoParcial)))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.libroId", Matchers.is(2)));

        verify(prestamoService, times(1)).modificarParcialmentePrestamo(eq(1L), any(PrestamoDTO.class));
    }

    @Test
    void eliminarPrestamo_existente() throws Exception {
        when(prestamoService.eliminarPrestamo(1L)).thenReturn(true);

        mockMvc.perform(delete("/prestamos/1"))
               .andExpect(status().isNoContent());

        verify(prestamoService, times(1)).eliminarPrestamo(1L);
    }


}


package com.example.demo.controller;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
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


@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private UsuarioService usuarioService;

    @Test
    void obtenerUsuarios() throws Exception {

        List<UsuarioDTO> usuariosMock = new ArrayList<>();

        LocalDate fecha1 = LocalDate.now();
        LocalDate fecha2 = LocalDate.now();


        UsuarioDTO usuario1 = new UsuarioDTO(1L, "Usuario1", "email1", "email1", fecha1);
        UsuarioDTO usuario2 =new UsuarioDTO(1L, "email2", "email2","email2",fecha2);

        usuariosMock.add(usuario1);
        usuariosMock.add(usuario2);

        when(usuarioService.obtenerUsuarios()).thenReturn(usuariosMock);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", Matchers.is(usuariosMock.size())))
               .andExpect(jsonPath("$[0].nombre", Matchers.is("Usuario1")))
               .andExpect(jsonPath("$[1].email", Matchers.is("email2")));

        verify(usuarioService, times (1)).obtenerUsuarios();

    }

    @Test
    void obtenerUsuarioId_existente() throws Exception {
        UsuarioDTO usuarioMock = new UsuarioDTO(1L, "Usuario", "email", "telefono", LocalDate.now());

        when(usuarioService.obtenerUsuarioId(1L)).thenReturn(usuarioMock);

        mockMvc.perform(get("/usuarios/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", Matchers.is("Usuario")))
               .andExpect(jsonPath("$.email", Matchers.is("email")));

        verify(usuarioService, times(1)).obtenerUsuarioId(1L);
    }

    @Test
    void obtenerUsuarioId_noExistente() throws Exception {
        when(usuarioService.obtenerUsuarioId(99L)).thenReturn(null);

        mockMvc.perform(get("/usuarios/99"))
               .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).obtenerUsuarioId(99L);
    }

    @Test
    void guardarUsuario() throws Exception {
        UsuarioDTO usuarioMock = new UsuarioDTO(1L, "Nuevo Usuario", "Nuevo email", "telefono", LocalDate.now());

        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioMock);

        mockMvc.perform(post("/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuarioMock)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", Matchers.is("Nuevo Usuario")))
               .andExpect(jsonPath("$.email", Matchers.is("Nuevo email")));

        verify(usuarioService, times(1)).guardarUsuario(any(UsuarioDTO.class));
    }

    @Test
    void modificarUsuario_existente() throws Exception {
        UsuarioDTO usuarioActualizado = new UsuarioDTO(1L, "Usuario modificado", "Email modificado", "Telefono modificado", LocalDate.now());

        when(usuarioService.modificarUsuario(eq(1L), any(UsuarioDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuarios/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuarioActualizado)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", Matchers.is("Usuario modificado")))
               .andExpect(jsonPath("$.email", Matchers.is("Email modificado")))
               .andExpect(jsonPath("$.telefono", Matchers.is("Telefono modificado")));

        verify(usuarioService, times(1)).modificarUsuario(eq(1L), any(UsuarioDTO.class));
    }

    @Test
    void modificarParcialmenteUsuario_existente() throws Exception {
        UsuarioDTO usuarioParcial = new UsuarioDTO();
        usuarioParcial.setNombre("Nuevo Nombre");

        UsuarioDTO usuarioActualizado = new UsuarioDTO(1L, "Nuevo Nombre", "email", "telefono", LocalDate.now());

        when(usuarioService.modificarParcialmenteUsuario(eq(1L), any(UsuarioDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(patch("/usuarios/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuarioParcial)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre", Matchers.is("Nuevo Nombre")));

        verify(usuarioService, times(1)).modificarParcialmenteUsuario(eq(1L), any(UsuarioDTO.class));
    }

    @Test
    void modificarParcialmenteUsuario_noExistente() throws Exception {
        when(usuarioService.modificarParcialmenteUsuario(eq(99L), any(UsuarioDTO.class))).thenReturn(null);

        mockMvc.perform(patch("/usuarios/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new UsuarioDTO())))
               .andExpect(status().isNotFound());

        verify(usuarioService, times(1)).modificarParcialmenteUsuario(eq(99L), any(UsuarioDTO.class));
    }

    @Test
    void eliminarUsuario_existente() throws Exception {
        when(usuarioService.eliminarUsuario(1L)).thenReturn(true);

        mockMvc.perform(delete("/usuarios/1"))
               .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }
}

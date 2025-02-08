package com.example.demo.service.impl;

import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
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

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    @BeforeEach
    void setUp(){ MockitoAnnotations.openMocks(this);}

    @Test
    void obtenerUsuarios() {

        List<UsuarioDAO> usuarios = new ArrayList<>();

        for (int i = 0; i<2;i++) {

            Long id = (long) 1;
            String nombre = "nombre"+i;
            String email = "email"+i;
            String telefono = "telefono"+i;
            LocalDate fechaPublicacion = LocalDate.now();
            UsuarioDAO usuario = new UsuarioDAO(
                    nombre,
                    email,
                    telefono,
                    fechaPublicacion
            );
            usuarios.add(usuario);
        }

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<UsuarioDTO> resultado = usuarioServiceImpl.obtenerUsuarios();

        assertEquals(2, resultado.size());

    }

    @Test
    void obtenerUsuarioId_existente() {
        UsuarioDAO usuarioMock = new UsuarioDAO("Usuario1", "Email1", "Telefono1", LocalDate.now());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        UsuarioDTO resultado = usuarioServiceImpl.obtenerUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals("Usuario1", resultado.getNombre());
    }

    @Test
    void obtenerUsuarioId_noExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        UsuarioDTO resultado = usuarioServiceImpl.obtenerUsuarioId(99L);

        assertNull(resultado);
    }

    @Test
    void guardarUsuario() {
        UsuarioDAO usuarioMock = new UsuarioDAO("Nuevo Usuario", "Nuevo Email", "telefono123", LocalDate.now());
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenReturn(usuarioMock);

        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "Nuevo Usuario", "Nuevo Email", "telefono123", LocalDate.now());
        UsuarioDTO resultado = usuarioServiceImpl.guardarUsuario(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("Nuevo Usuario", resultado.getNombre());
    }

    @Test
    void modificarUsuario_existente() {
        UsuarioDAO usuarioMock = new UsuarioDAO("Usuario1", "Email1", "Telefono1", LocalDate.now());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenReturn(usuarioMock);

        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "Usuario Modificado", "Email Modificado", "telefono999", LocalDate.now());
        UsuarioDTO resultado = usuarioServiceImpl.modificarUsuario(1L, usuarioDTO);

        assertNotNull(resultado);
        assertEquals("Usuario Modificado", resultado.getNombre());
    }

    @Test
    void modificarUsuario_noExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DatosInvalidosException.class, () ->
                usuarioServiceImpl.modificarUsuario(99L, new UsuarioDTO())
        );
    }


    @Test
    void modificarParcialmenteUsuario_existente() {
        UsuarioDAO usuarioMock = new UsuarioDAO("Usuario1", "Email1", "Telefono1", LocalDate.now());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(UsuarioDAO.class))).thenReturn(usuarioMock);

        UsuarioDTO usuarioParcial = new UsuarioDTO();
        usuarioParcial.setNombre("Nuevo Título");

        UsuarioDTO resultado = usuarioServiceImpl.modificarParcialmenteUsuario(1L, usuarioParcial);

        assertNotNull(resultado);
        assertEquals("Nuevo Título", resultado.getNombre());
    }

    @Test
    void modificarParcialmenteUsuario_noExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        UsuarioDTO resultado = usuarioServiceImpl.modificarParcialmenteUsuario(99L, new UsuarioDTO());

        assertNull(resultado);
    }

    @Test
    void eliminarUsuario_existente() {
        UsuarioDAO usuarioMock = new UsuarioDAO("Usuario1", "Email1", "Telefono1", LocalDate.now());
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        boolean resultado = usuarioServiceImpl.eliminarUsuario(1L);

        assertTrue(resultado);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarUsuario_noExistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioServiceImpl.eliminarUsuario(99L));
    }

}


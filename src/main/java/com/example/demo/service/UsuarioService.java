package com.example.demo.service;


import com.example.demo.dto.UsuarioDTO;

import java.util.List;


public interface UsuarioService {

    List<UsuarioDTO> obtenerUsuarios();

    UsuarioDTO obtenerUsuarioId(Long id);

    UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO modificarUsuario(Long id, UsuarioDTO usuarioDTO);

    UsuarioDTO modificarParcialmenteUsuario(Long id, UsuarioDTO usuarioDTO);

    boolean eliminarUsuario(Long id);
}
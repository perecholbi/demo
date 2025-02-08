package com.example.demo.mapper;

import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dto.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO usuarioDaoToUsuarioDto(UsuarioDAO usuarioDao);

    List<UsuarioDTO> usuarioDaosToUsuarioDtos(List<UsuarioDAO> usuarioDao);

    UsuarioDAO usuarioDtoToUsuarioDao(UsuarioDTO usuarioDTO);
}

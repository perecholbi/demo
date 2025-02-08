package com.example.demo.mapper;

import com.example.demo.dao.PrestamoDAO;
import com.example.demo.dto.PrestamoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PrestamoMapper {

    PrestamoMapper INSTANCE = Mappers.getMapper(PrestamoMapper.class);

    @Mapping(source = "libro.id", target = "libroId")
    @Mapping(source = "usuario.id", target = "usuarioId")
    PrestamoDTO prestamoDaoToPrestamoDto(PrestamoDAO prestamoDao);

    List<PrestamoDTO> prestamosDaoToPrestamosDto(List<PrestamoDAO> prestamosDao);

    @Mapping(source = "libroId", target = "libro.id")
    @Mapping(source = "usuarioId", target = "usuario.id")
    PrestamoDAO prestamoDtoToPrestamoDao(PrestamoDTO prestamoDTO);
}
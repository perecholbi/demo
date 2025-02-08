package com.example.demo.mapper;

import com.example.demo.dao.LibroDAO;
import com.example.demo.dto.LibroDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LibroMapper {

    LibroMapper INSTANCE = Mappers.getMapper(LibroMapper.class);

    LibroDTO libroDaoToLibroDto(LibroDAO libroDao);

    List<LibroDTO> libroDaosToLibroDtos(List<LibroDAO> libroDao);

    LibroDAO libroDtoToLibroDao(LibroDTO libroDTO);
}

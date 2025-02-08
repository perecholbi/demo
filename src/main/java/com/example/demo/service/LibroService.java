package com.example.demo.service;


import com.example.demo.dto.LibroDTO;

import java.util.List;


public interface LibroService {

    List<LibroDTO> obtenerLibros();

    LibroDTO obtenerLibroId(Long id);

    LibroDTO guardarLibro(LibroDTO libroDTO);

    LibroDTO modificarLibro(Long id, LibroDTO libroDTO);

    LibroDTO modificarParcialmenteLibro(Long id, LibroDTO libroDTO);

    boolean eliminarLibro(Long id);
}

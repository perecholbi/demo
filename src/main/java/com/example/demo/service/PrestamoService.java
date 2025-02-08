package com.example.demo.service;


import com.example.demo.dto.PrestamoDTO;

import java.util.List;


public interface PrestamoService {

    List<PrestamoDTO> obtenerPrestamos();

    PrestamoDTO obtenerPrestamoId(Long id);

    PrestamoDTO guardarPrestamo(PrestamoDTO prestamoDTO);

    PrestamoDTO modificarPrestamo(Long id, PrestamoDTO prestamoDTO);

    PrestamoDTO modificarParcialmentePrestamo(Long id, PrestamoDTO prestamoDTO);

    boolean eliminarPrestamo(Long id);
}

package com.example.demo.controller;

import com.example.demo.dto.PrestamoDTO;
import com.example.demo.dto.PrestamoDTO;
import com.example.demo.dto.PrestamoDTO;
import com.example.demo.service.PrestamoService;
import com.example.demo.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;


    @GetMapping
    ResponseEntity<List<PrestamoDTO>> obtenerPrestamos(){

        log.info("PrestamoController obtenerPrestamos...");

        List<PrestamoDTO> resultado = prestamoService.obtenerPrestamos();

        log.info("PrestamoController obtenerPrestamos se han obtenido {} resultaods", resultado.size());

        ResponseEntity<List<PrestamoDTO>> respuesta = ResponseEntity.ok(resultado);
        return respuesta;

    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoDTO> obtenerPrestamoId(@PathVariable Long id) {
        log.info("PrestamoController obtenerPrestamoId con id: {}", id);
        PrestamoDTO prestamo = prestamoService.obtenerPrestamoId(id);
        if (prestamo != null) {
            return ResponseEntity.ok(prestamo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    PrestamoDTO guardarPrestamo(@RequestBody PrestamoDTO prestamoDTO) {
        return prestamoService.guardarPrestamo(prestamoDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestamoDTO> modificarPrestamo(@PathVariable("id") Long id, @RequestBody PrestamoDTO prestamoDTO) {
        PrestamoDTO prestamoActualizado = prestamoService.modificarPrestamo(id, prestamoDTO);
        return ResponseEntity.ok(prestamoActualizado);
    }

    @PatchMapping("/{id}")
    ResponseEntity<PrestamoDTO> modificarParcialmentePrestamo(@PathVariable("id") Long id, @RequestBody PrestamoDTO prestamoDTO) {
        PrestamoDTO prestamoActualizado = prestamoService.modificarParcialmentePrestamo(id, prestamoDTO);
        return ResponseEntity.ok(prestamoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        boolean eliminado = prestamoService.eliminarPrestamo(id);
            return ResponseEntity.noContent().build();
    }



}

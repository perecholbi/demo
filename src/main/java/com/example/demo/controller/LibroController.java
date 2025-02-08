package com.example.demo.controller;


import com.example.demo.dto.LibroDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.service.LibroService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
@RequestMapping("/libros")
public class LibroController {


    @Autowired
    private LibroService libroService;

    @GetMapping
    ResponseEntity<List<LibroDTO>> obtenerLibros(){

        log.info("LibroController obtenerLibros...");

        List<LibroDTO> resultado = libroService.obtenerLibros();

        log.info("LibroController obtenerLibros se han obtenido {} resultaods", resultado.size());

        ResponseEntity<List<LibroDTO>> respuesta = ResponseEntity.ok(resultado);
       return respuesta;

    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> obtenerLibroId(@PathVariable Long id) {
        log.info("LibroController obtenerLibroId con id: {}", id);
        LibroDTO libro = libroService.obtenerLibroId(id);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<LibroDTO> guardarLibro(@RequestBody LibroDTO libroDTO) {
            LibroDTO libroGuardado = libroService.guardarLibro(libroDTO);
            return ResponseEntity.ok(libroGuardado);

    }


    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> modificarLibro(@PathVariable("id") Long id, @RequestBody LibroDTO libroDTO) {
            LibroDTO libroActualizado = libroService.modificarLibro(id, libroDTO);
            return ResponseEntity.ok(libroActualizado);
    }

    @PatchMapping("/{id}")
    ResponseEntity<LibroDTO> modificarParcialmenteLibro(@PathVariable("id") Long id, @RequestBody LibroDTO libroDTO) {
        LibroDTO libroActualizado = libroService.modificarParcialmenteLibro(id, libroDTO);

        if (libroActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
    }




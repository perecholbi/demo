package com.example.demo.controller;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.dto.UsuarioDTO;
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
@RequestMapping("/usuarios")
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    ResponseEntity<List<UsuarioDTO>> obtenerUsuarios(){

        log.info("UsuarioController obtenerUsuarios...");

        List<UsuarioDTO> resultado = usuarioService.obtenerUsuarios();

        log.info("UsuarioController obtenerUsuarios se han obtenido {} resultaods", resultado.size());

        ResponseEntity<List<UsuarioDTO>> respuesta = ResponseEntity.ok(resultado);
        return respuesta;

    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioId(@PathVariable Long id) {
        log.info("UsuarioController obtenerUsuarioId con id: {}", id);
        UsuarioDTO usuario = usuarioService.obtenerUsuarioId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    UsuarioDTO guardarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.guardarUsuario(usuarioDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> modificarUsuario(@PathVariable("id") Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.modificarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PatchMapping("/{id}")
    ResponseEntity<UsuarioDTO> modificarParcialmenteUsuario(@PathVariable("id") Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.modificarParcialmenteUsuario(id, usuarioDTO);

        if (usuarioActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}



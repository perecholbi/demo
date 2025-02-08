package com.example.demo.service.impl;

import com.example.demo.controller.UsuarioController;
import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.repository.PrestamoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    private static final UsuarioMapper usuarioMapper = UsuarioMapper.INSTANCE;

    @Override
    public List<UsuarioDTO> obtenerUsuarios(){

        log.info("UsuarioController obtenerUsuarios...");

        List<UsuarioDAO> result = usuarioRepository.findAll();

        List<UsuarioDTO> resultado = usuarioMapper.usuarioDaosToUsuarioDtos(result);

        log.info("UsuarioController obtenerUsuarios se han obtenido {} resultaods", resultado.size());

        return resultado;
    }

    @Override
    public UsuarioDTO obtenerUsuarioId(Long id){
        log.info("UsuarioController obtenerUsuario...");
        Optional<UsuarioDAO> result = usuarioRepository.findById(id);

        if (result.isPresent()) {
            return usuarioMapper.usuarioDaoToUsuarioDto(result.get());
        } else {
            return null;
        }
    }

    @Override
    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Creando un nuevo usuario...");
        if (usuarioDTO == null ||  usuarioDTO.getNombre() == null || usuarioDTO.getEmail() == null || usuarioDTO.getTelefono() == null || usuarioDTO.getFechaRegistro() == null) {
            log.error("Datos invalidos");
            throw new DatosInvalidosException("Los datos del libro son inválidos o están incompletos");
        }
        UsuarioDAO usuarioDAO = usuarioMapper.usuarioDtoToUsuarioDao(usuarioDTO);
        usuarioDAO = usuarioRepository.save(usuarioDAO);
        UsuarioDTO resultado = usuarioMapper.usuarioDaoToUsuarioDto(usuarioDAO);
        log.info("Usuario creado con éxito: {}", resultado);

        return resultado;
    }

    @Override
    public UsuarioDTO modificarUsuario(Long id, UsuarioDTO usuarioDTO) {
        log.info("Modificando usuario con ID: {}", id);
        if (usuarioDTO == null || usuarioDTO.getNombre() == null || usuarioDTO.getEmail() == null || usuarioDTO.getFechaRegistro() == null) {
            log.error("Datos invalidos");
            throw new DatosInvalidosException("Los datos del usuario son inválidos o están incompletos");
        }

        Optional<UsuarioDAO> result = usuarioRepository.findById(id);

        if (result.isPresent()) {
            UsuarioDAO usuarioExistente = result.get();

            usuarioExistente.setEmail(usuarioDTO.getEmail());
            usuarioExistente.setNombre(usuarioDTO.getNombre());
            usuarioExistente.setTelefono(usuarioDTO.getTelefono());
            usuarioExistente.setFechaRegistro(usuarioDTO.getFechaRegistro());

            usuarioExistente = usuarioRepository.save(usuarioExistente);

            UsuarioDTO resultado = usuarioMapper.usuarioDaoToUsuarioDto(usuarioExistente);

            log.info("Usuario modificado con éxito: {}", resultado);

            return resultado;
        } else {
            log.info("No se encontró el usuario con ID: {}", id);
            throw new RecursoNoEncontradoException("No existe el usuario");
        }
    }

    @Override
    public UsuarioDTO modificarParcialmenteUsuario(Long id, UsuarioDTO usuarioDTO) {
        log.info("Modificando parcialmente usuario con ID: {}", id);

        Optional<UsuarioDAO> result = usuarioRepository.findById(id);
        if (!result.isPresent()) {
            log.info("No se encontró el usuario con ID: {}", id);
            return null;
        }


        UsuarioDAO usuarioExistente = result.get();
        if (usuarioDTO.getEmail() != null) {
            usuarioExistente.setEmail(usuarioDTO.getEmail());
        }
        if (usuarioDTO.getNombre() != null) {
            usuarioExistente.setNombre(usuarioDTO.getNombre());
        }
        if (usuarioDTO.getTelefono() != null) {
            usuarioExistente.setTelefono(usuarioDTO.getTelefono());
        }
        if (usuarioDTO.getFechaRegistro() != null) {
            usuarioExistente.setFechaRegistro(usuarioDTO.getFechaRegistro());
        }

        usuarioExistente = usuarioRepository.save(usuarioExistente);
        UsuarioDTO resultado = usuarioMapper.usuarioDaoToUsuarioDto(usuarioExistente);

        log.info("Usuario modificado parcialmente con éxito: {}", resultado);
        return resultado;
    }


    @Override
    public boolean eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        Optional<UsuarioDAO> result = usuarioRepository.findById(id);
        if (prestamoRepository.existsByUsuarioId(id)) {
            throw new IllegalStateException("No se puede eliminar el usuario porque tiene préstamos asociados.");
        }
        if (result.isPresent()) {
            usuarioRepository.deleteById(id);
            log.info("Usuario con ID: {} eliminado con éxito", id);
            return true;
        } else {
            log.error("No se encontró el usuario con ID: {}", id);
            throw new RecursoNoEncontradoException("No se encontró el usuario con ID: " + id);
        }
    }
}


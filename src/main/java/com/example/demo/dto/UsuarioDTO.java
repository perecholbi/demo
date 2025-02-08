package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@Getter
@Setter

public class UsuarioDTO {

    private Long id;

    private String nombre;

    private String email;

    private String telefono;

    private LocalDate fechaRegistro;


    public UsuarioDTO() {

    }
}
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

public class PrestamoDTO {

    private Long id;
    private Long libroId;
    private Long usuarioId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public PrestamoDTO() {}


}

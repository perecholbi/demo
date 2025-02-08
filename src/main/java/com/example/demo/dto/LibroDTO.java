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

public class LibroDTO {

    private Long id;

    private String titulo;

    private String autor;

    private String isbn;

    private LocalDate fechaPublicacion;


    public LibroDTO() {

    }
}

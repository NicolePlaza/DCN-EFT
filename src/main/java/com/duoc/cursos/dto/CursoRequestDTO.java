package com.duoc.cursos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CursoRequestDTO {

    @NotBlank(message = "el nombre del curso es obligatorio")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "el instructor es obligatorio")
    private String instructor;

    @NotNull(message = "los cupos son obligatorios")
    private Integer cupos;

    private BigDecimal valor;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}

package com.duoc.cursos.dto;

import java.io.Serializable;

public class InscripcionMensajeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rutEstudiante;
    private String nombreEstudiante;
    private String correoEstudiante;
    private Long idCurso;
    private String nombreCurso;
    private String motivoError;

    public InscripcionMensajeDTO() {
    }

    public InscripcionMensajeDTO(String rutEstudiante, String nombreEstudiante, String correoEstudiante,
                                 Long idCurso, String nombreCurso) {
        this.rutEstudiante = rutEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.correoEstudiante = correoEstudiante;
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
    }

    public String getRutEstudiante() { return rutEstudiante; }
    public void setRutEstudiante(String rutEstudiante) { this.rutEstudiante = rutEstudiante; }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

    public String getCorreoEstudiante() { return correoEstudiante; }
    public void setCorreoEstudiante(String correoEstudiante) { this.correoEstudiante = correoEstudiante; }

    public Long getIdCurso() { return idCurso; }
    public void setIdCurso(Long idCurso) { this.idCurso = idCurso; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public String getMotivoError() { return motivoError; }
    public void setMotivoError(String motivoError) { this.motivoError = motivoError; }

    @Override
    public String toString() {
        return "InscripcionMensajeDTO{rutEstudiante='" + rutEstudiante + "', nombreEstudiante='" + nombreEstudiante +
                "', idCurso=" + idCurso + ", nombreCurso='" + nombreCurso + "'" +
                (motivoError != null ? ", motivoError='" + motivoError + "'" : "") + "}";
    }
}

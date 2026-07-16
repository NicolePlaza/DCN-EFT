package com.duoc.cursos.controller;

import com.duoc.cursos.dto.InscripcionMensajeDTO;
import com.duoc.cursos.model.InscripcionProcesada;
import com.duoc.cursos.repository.InscripcionProcesadaRepository;
import com.duoc.cursos.service.InscripcionProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionProducerService producerService;
    private final InscripcionProcesadaRepository repository;

    public InscripcionController(InscripcionProducerService producerService,
                                 InscripcionProcesadaRepository repository) {
        this.producerService = producerService;
        this.repository = repository;
    }

    @PreAuthorize("hasAnyAuthority('APPROLE_Estudiante','APPROLE_Instructor')")
    @PostMapping
    public ResponseEntity<String> inscribir(@RequestBody InscripcionMensajeDTO mensaje) {
        producerService.procesarEnvio(mensaje);
        return ResponseEntity.status(202)
                .body("Inscripcion encolada correctamente para: " + mensaje.getNombreEstudiante());
    }

    @PreAuthorize("hasAnyAuthority('APPROLE_Estudiante','APPROLE_Instructor')")
    @PostMapping("/error")
    public ResponseEntity<String> simularError(@RequestBody InscripcionMensajeDTO mensaje) {
        producerService.enviarAColaError(mensaje, "Inscripcion con datos invalidos o error de procesamiento");
        return ResponseEntity.status(202)
                .body("Inscripcion derivada a la cola de errores correctamente");
    }

    @PreAuthorize("hasAnyAuthority('APPROLE_Estudiante','APPROLE_Instructor')")
    @GetMapping
    public ResponseEntity<List<InscripcionProcesada>> consultar() {
        List<InscripcionProcesada> inscripciones = repository.findAll();
        if (inscripciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inscripciones);
    }
}

package com.duoc.cursos.controller;

import com.duoc.cursos.dto.CursoRequestDTO;
import com.duoc.cursos.model.Curso;
import com.duoc.cursos.service.CursoService;
import com.duoc.cursos.service.S3Service;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final S3Service s3Service;

    public CursoController(CursoService cursoService, S3Service s3Service) {
        this.cursoService = cursoService;
        this.s3Service = s3Service;
    }

    @PreAuthorize("hasAuthority('APPROLE_Instructor')")
    @PostMapping
    public ResponseEntity<Curso> crear(@Valid @RequestBody CursoRequestDTO request) {
        return ResponseEntity.status(201).body(cursoService.crear(request));
    }

    @PreAuthorize("hasAnyAuthority('APPROLE_Instructor','APPROLE_Estudiante')")
    @GetMapping
    public ResponseEntity<List<Curso>> consultar(@RequestParam(required = false) String instructor) {
        return ResponseEntity.ok(cursoService.consultar(instructor));
    }

    @PreAuthorize("hasAuthority('APPROLE_Instructor')")
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @Valid @RequestBody CursoRequestDTO request) {
        return cursoService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('APPROLE_Instructor')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (cursoService.eliminar(id)) {
            return ResponseEntity.ok("Curso " + id + " eliminado");
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('APPROLE_Instructor')")
    @PostMapping("/{id}/material")
    public ResponseEntity<?> subirMaterial(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        if (cursoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            String key = "curso-" + id + ".pdf";
            s3Service.subirMaterial(key, archivo.getBytes());
            Curso curso = cursoOpt.get();
            curso.setS3Key(key);
            cursoService.guardar(curso);
            return ResponseEntity.ok("Material subido a S3 con key: " + key);
        } catch (java.io.IOException e) {
            return ResponseEntity.status(500).body("Error al leer el archivo: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('APPROLE_Instructor','APPROLE_Estudiante')")
    @GetMapping("/{id}/material")
    public ResponseEntity<byte[]> descargarMaterial(@PathVariable Long id) {
        Optional<Curso> cursoOpt = cursoService.buscarPorId(id);
        if (cursoOpt.isEmpty() || cursoOpt.get().getS3Key() == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] contenido = s3Service.descargarMaterial(cursoOpt.get().getS3Key());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cursoOpt.get().getS3Key() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido);
    }
}

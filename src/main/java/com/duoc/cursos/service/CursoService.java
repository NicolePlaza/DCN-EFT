package com.duoc.cursos.service;

import com.duoc.cursos.dto.CursoRequestDTO;
import com.duoc.cursos.model.Curso;
import com.duoc.cursos.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public Curso crear(CursoRequestDTO request) {
        Curso curso = new Curso();
        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setInstructor(request.getInstructor());
        curso.setCupos(request.getCupos());
        curso.setValor(request.getValor());
        return repository.save(curso);
    }

    public Optional<Curso> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Curso> actualizar(Long id, CursoRequestDTO request) {
        return repository.findById(id).map(curso -> {
            curso.setNombre(request.getNombre());
            curso.setDescripcion(request.getDescripcion());
            curso.setInstructor(request.getInstructor());
            curso.setCupos(request.getCupos());
            curso.setValor(request.getValor());
            return repository.save(curso);
        });
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Curso> consultar(String instructor) {
        if (instructor != null) {
            return repository.findByInstructor(instructor);
        }
        return repository.findAll();
    }

    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }
}

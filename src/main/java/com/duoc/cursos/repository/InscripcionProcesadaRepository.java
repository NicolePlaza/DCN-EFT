package com.duoc.cursos.repository;

import com.duoc.cursos.model.InscripcionProcesada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionProcesadaRepository extends JpaRepository<InscripcionProcesada, Long> {
}

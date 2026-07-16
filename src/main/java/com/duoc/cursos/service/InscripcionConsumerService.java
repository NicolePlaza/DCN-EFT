package com.duoc.cursos.service;

import com.duoc.cursos.config.RabbitMQConfig;
import com.duoc.cursos.dto.InscripcionMensajeDTO;
import com.duoc.cursos.model.InscripcionProcesada;
import com.duoc.cursos.repository.InscripcionProcesadaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class InscripcionConsumerService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionConsumerService.class);

    private final InscripcionProcesadaRepository repository;

    public InscripcionConsumerService(InscripcionProcesadaRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfig.INSCRIPCION_QUEUE)
    public void consumirInscripcion(InscripcionMensajeDTO mensaje) {
        log.info("Inscripcion recibida desde la cola principal: {}", mensaje);

        InscripcionProcesada procesada = new InscripcionProcesada();
        procesada.setRutEstudiante(mensaje.getRutEstudiante());
        procesada.setNombreEstudiante(mensaje.getNombreEstudiante());
        procesada.setCorreoEstudiante(mensaje.getCorreoEstudiante());
        procesada.setIdCurso(mensaje.getIdCurso());
        procesada.setNombreCurso(mensaje.getNombreCurso());

        InscripcionProcesada guardada = repository.save(procesada);
        log.info("Inscripcion guardada en Oracle (tabla INSCRIPCION_PROCESADA) con ID: {}", guardada.getId());
    }

    @RabbitListener(queues = RabbitMQConfig.INSCRIPCION_ERROR_QUEUE)
    public void consumirInscripcionError(InscripcionMensajeDTO mensaje) {
        log.warn("Inscripcion con ERROR recibida desde la cola de errores: {} | Motivo: {}",
                mensaje, mensaje.getMotivoError());
    }
}

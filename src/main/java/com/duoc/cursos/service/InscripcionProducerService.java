package com.duoc.cursos.service;

import com.duoc.cursos.config.RabbitMQConfig;
import com.duoc.cursos.dto.InscripcionMensajeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class InscripcionProducerService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionProducerService.class);

    private final RabbitTemplate rabbitTemplate;

    public InscripcionProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void procesarEnvio(InscripcionMensajeDTO mensaje) {
        String errorValidacion = validar(mensaje);
        if (errorValidacion != null) {
            enviarAColaError(mensaje, "Validacion fallida: " + errorValidacion);
            return;
        }
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.INSCRIPCION_EXCHANGE,
                    RabbitMQConfig.INSCRIPCION_ROUTING_KEY,
                    mensaje);
            log.info("Inscripcion enviada a la cola principal: {}", mensaje);
        } catch (Exception e) {
            log.error("Error al enviar la inscripcion, se deriva a la cola de errores", e);
            enviarAColaError(mensaje, "Error de procesamiento: " + e.getMessage());
        }
    }

    public void enviarAColaError(InscripcionMensajeDTO mensaje, String motivo) {
        mensaje.setMotivoError(motivo);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.INSCRIPCION_EXCHANGE,
                RabbitMQConfig.INSCRIPCION_ERROR_ROUTING_KEY,
                mensaje);
        log.warn("Inscripcion enviada a la cola de errores. Motivo: {}", motivo);
    }

    private String validar(InscripcionMensajeDTO m) {
        if (m.getRutEstudiante() == null || m.getRutEstudiante().isBlank()) {
            return "el rut del estudiante es obligatorio";
        }
        if (m.getNombreEstudiante() == null || m.getNombreEstudiante().isBlank()) {
            return "el nombre del estudiante es obligatorio";
        }
        if (m.getIdCurso() == null) {
            return "el curso es obligatorio";
        }
        return null;
    }
}

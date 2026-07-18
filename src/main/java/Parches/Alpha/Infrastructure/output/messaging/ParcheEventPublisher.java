package Parches.Alpha.Infrastructure.output.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Publica los eventos de parche en notification.exchange. Los consumen:
 * - chat-service (parche.created / parche.member-joined): crea la sala
 *   grupal del parche y agrega miembros (id de sala == parcheId).
 * - GamificationService (mismas routing keys): monas PATCH_CREATED /
 *   PATCH_JOINED (Capitán de Equipo, Iniciador de Parche, etc.).
 *
 * Best-effort: si RabbitMQ no está disponible, el flujo principal del
 * parche NO se rompe (chat-service tiene un ensure idempotente que crea la
 * sala bajo demanda, y la mona simplemente no progresa esa vez).
 */
@Component
public class ParcheEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ParcheEventPublisher.class);

    private static final String PARCHE_CREATED_KEY = "parche.created";
    private static final String PARCHE_MEMBER_JOINED_KEY = "parche.member-joined";

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;

    public ParcheEventPublisher(RabbitTemplate rabbitTemplate,
                                @Value("${rabbitmq.exchange.notification:notification.exchange}") String exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    /** Espejo de los DTO consumidores (chat-service y GamificationService). */
    public record ParcheCreatedEvent(String parcheId, String creatorId, String category) {
    }

    public record ParcheMemberJoinedEvent(String parcheId, String studentId) {
    }

    public void publishParcheCreated(UUID parcheId, UUID creatorId, String category) {
        try {
            rabbitTemplate.convertAndSend(exchange, PARCHE_CREATED_KEY,
                    new ParcheCreatedEvent(parcheId.toString(), creatorId.toString(), category));
        } catch (Exception e) {
            log.warn("No se pudo publicar parche.created (parcheId={}): {}", parcheId, e.getMessage());
        }
    }

    public void publishMemberJoined(UUID parcheId, UUID studentId) {
        try {
            rabbitTemplate.convertAndSend(exchange, PARCHE_MEMBER_JOINED_KEY,
                    new ParcheMemberJoinedEvent(parcheId.toString(), studentId.toString()));
        } catch (Exception e) {
            log.warn("No se pudo publicar parche.member-joined (parcheId={}, studentId={}): {}",
                    parcheId, studentId, e.getMessage());
        }
    }
}

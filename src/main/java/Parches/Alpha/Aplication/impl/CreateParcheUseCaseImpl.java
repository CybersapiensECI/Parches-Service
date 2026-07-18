package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.exception.MaxActiveParchesException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.CreateParcheUseCase;
import Parches.Alpha.Domain.Enums.*;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import Parches.Alpha.Infrastructure.messaging.ParcheCreatedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class CreateParcheUseCaseImpl implements CreateParcheUseCase {

    private final ParcheRepositorySPI parcheRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.notification:notification.exchange}")
    private String notificationExchange;

    @Autowired
    public CreateParcheUseCaseImpl(ParcheRepositorySPI parcheRepository, RabbitTemplate rabbitTemplate) {
        this.parcheRepository = parcheRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UUID execute(CreateParcheCommand command) {
        long activeCount = parcheRepository.countActiveParchesByStudentId(command.creatorStudentId());
        if (activeCount >= 5) {
            throw new MaxActiveParchesException("El estudiante ya superó el máximo de 5 parches activos.");
        }

        if (command.maximumQuota() < 2 || command.maximumQuota() > 30) {
            throw new ParcheFullException("Cupo inválido. El cupo debe estar entre 2 y 30.");
        }

        Parche parche = Parche.builder()
                .id(UUID.randomUUID())
                .name(command.name())
                .description(command.description())
                .place(Places.valueOf(command.place().toUpperCase()))
                .category(ParcheCategory.valueOf(command.category().toUpperCase()))
                .type(ParcheType.valueOf(command.type().toUpperCase()))
                .date(command.date())
                .hour(command.hour())
                .maximumQuota(command.maximumQuota())
                .status(ParcheStatus.ACTIVE)
                .creatorStudentId(command.creatorStudentId())
                .creationDate(LocalDateTime.now())
                .allowedMemberInvitation(true)
                .eventId(command.eventId())
                .members(new ArrayList<>())
                .posts(new ArrayList<>())
                .invitations(new ArrayList<>())
                .build();

        parche.addMember(command.creatorStudentId(), MemberRole.CREATOR);

        UUID id = parcheRepository.save(parche);
        publishParcheCreated(id, command.creatorStudentId(), parche.getCategory());
        return id;
    }

    /**
     * chat-service crea la sala grupal y GamificationService desbloquea
     * monas a partir de esto. No debe tumbar la creación del parche si
     * RabbitMQ está caído: se registra y se sigue.
     */
    private void publishParcheCreated(UUID parcheId, UUID creatorId, ParcheCategory category) {
        try {
            rabbitTemplate.convertAndSend(
                    notificationExchange,
                    "parche.created",
                    ParcheCreatedMessage.builder()
                            .parcheId(parcheId)
                            .creatorId(creatorId)
                            .category(category == null ? null : category.name())
                            .build());
        } catch (Exception e) {
            log.warn("No se pudo publicar el parche creado {} para el chat grupal: {}",
                    parcheId, e.getMessage());
        }
    }
}

package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.JoinParcheUseCase;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Enums.ParcheType;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import Parches.Alpha.Infrastructure.messaging.ParcheMemberJoinedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
public class JoinParcheUseCaseImpl implements JoinParcheUseCase {

    private final ParcheRepositorySPI parcheRepository;
    private final InvitationRepositorySPI invitationRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.notification:notification.exchange}")
    private String notificationExchange;

    @Autowired
    public JoinParcheUseCaseImpl(ParcheRepositorySPI parcheRepository, InvitationRepositorySPI invitationRepository,
                                  RabbitTemplate rabbitTemplate) {
        this.parcheRepository = parcheRepository;
        this.invitationRepository = invitationRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void execute(UUID parcheId, UUID studentId) {
        Parche parche = parcheRepository.findById(parcheId)
                .orElseThrow(() -> new RuntimeException("Parche no existe."));

        if (parche.isStudentMember(studentId)) {
            throw new RuntimeException("El estudiante ya es miembro de este parche.");
        }

        if (parche.getMembers() != null && parche.getMembers().size() >= parche.getMaximumQuota()) {
            throw new ParcheFullException("El parche está lleno.");
        }

        if (parche.getType() == ParcheType.PRIVATE) {
            boolean hasInvitation = invitationRepository.existsPendingInvitation(parcheId, studentId);
            if (!hasInvitation) {
                throw new ActionNotAllowedException("No puedes unirte a un parche privado sin una invitación.");
            }
        }

        parche.addMember(studentId, MemberRole.STUDENT);
        parcheRepository.save(parche);
        publishMemberJoined(parcheId, studentId);
    }

    /**
     * chat-service agrega al miembro a la sala grupal a partir de esto. No
     * debe tumbar el join si RabbitMQ está caído: se registra y se sigue.
     */
    private void publishMemberJoined(UUID parcheId, UUID studentId) {
        try {
            rabbitTemplate.convertAndSend(
                    notificationExchange,
                    "parche.member-joined",
                    ParcheMemberJoinedMessage.builder()
                            .parcheId(parcheId)
                            .studentId(studentId)
                            .build());
        } catch (Exception e) {
            log.warn("No se pudo publicar el miembro {} del parche {} para el chat grupal: {}",
                    studentId, parcheId, e.getMessage());
        }
    }
}

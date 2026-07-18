package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.AcceptInvitationUseCase;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Model.Invitation;
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
public class AcceptInvitationUseCaseImpl implements AcceptInvitationUseCase {

    private final InvitationRepositorySPI invitationRepository;
    private final ParcheRepositorySPI parcheRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.notification:notification.exchange}")
    private String notificationExchange;

    @Autowired
    public AcceptInvitationUseCaseImpl(InvitationRepositorySPI invitationRepository, ParcheRepositorySPI parcheRepository,
                                       RabbitTemplate rabbitTemplate) {
        this.invitationRepository = invitationRepository;
        this.parcheRepository = parcheRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void execute(UUID invitationId, UUID studentId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitación no encontrada."));

        if (!invitation.getInvitedStudentId().equals(studentId)) {
            throw new ActionNotAllowedException("No puedes aceptar una invitación dirigida a otro estudiante.");
        }

        Parche parche = parcheRepository.findById(invitation.getParcheId())
                .orElseThrow(() -> new RuntimeException("El parche asociado a esta invitación no existe."));

        if (parche.getStatus() != ParcheStatus.ACTIVE) {
            throw new RuntimeException("El parche al que fuiste invitado ya no está activo o ha sido archivado.");
        }

        if (parche.getMembers() != null && parche.getMembers().size() >= parche.getMaximumQuota()) {
            throw new ParcheFullException("No hay cupo disponible en el parche para aceptar la invitación.");
        }

        invitation.accept();
        parche.addMember(studentId, MemberRole.STUDENT);

        invitationRepository.save(invitation);
        parcheRepository.save(parche);
        publishMemberJoined(parche.getId(), studentId);
    }

    /**
     * Aceptar una invitacion tambien agrega un miembro: mismo evento que el
     * join directo, para que chat-service lo sume a la sala grupal y
     * GamificationService acredite PATCH_JOINED. Best-effort: no tumba la
     * aceptacion si RabbitMQ esta caido.
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

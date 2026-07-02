package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.SendInvitationCommand;
import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.DuplicateInvitationException;
import Parches.Alpha.Aplication.ports.SendInvitationUseCase;
import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.State.PendingState;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class SendInvitationUseCaseImpl implements SendInvitationUseCase {

    private final ParcheRepositorySPI parcheRepository;
    private final InvitationRepositorySPI invitationRepository;

    @Autowired
    public SendInvitationUseCaseImpl(ParcheRepositorySPI parcheRepository, InvitationRepositorySPI invitationRepository) {
        this.parcheRepository = parcheRepository;
        this.invitationRepository = invitationRepository;
    }

    @Override
    public UUID execute(SendInvitationCommand command) {
        Parche parche = parcheRepository.findById(command.parcheId())
                .orElseThrow(() -> new RuntimeException("Parche no encontrado."));

        if (!parche.isStudentMember(command.senderId())) {
            throw new ActionNotAllowedException("El remitente debe ser miembro del parche para enviar invitaciones.");
        }

        boolean isCreator = parche.getCreatorStudentId().equals(command.senderId());
        if (!isCreator && !parche.isAllowedMemberInvitation()) {
            throw new Parches.Alpha.Domain.exception.DomainException("El parche no permite que los miembros comunes envíen invitaciones.");
        }

        boolean existsPending = invitationRepository.existsPendingInvitation(command.parcheId(), command.invitedId());
        if (existsPending) {
            throw new DuplicateInvitationException("Ya existe una invitación pendiente para este estudiante a este parche.");
        }

        if (parche.isStudentMember(command.invitedId())) {
            throw new RuntimeException("El estudiante ya es miembro de este parche.");
        }

        Invitation invitation = Invitation.builder()
                .id(UUID.randomUUID())
                .parcheId(command.parcheId())
                .inviterId(command.senderId())
                .invitedStudentId(command.invitedId())
                .state(new PendingState())
                .sentAt(LocalDateTime.now())
                .build();

        parche.inviteStudent(invitation);

        invitationRepository.save(invitation);
        parcheRepository.save(parche);

        return invitation.getId();
    }
}

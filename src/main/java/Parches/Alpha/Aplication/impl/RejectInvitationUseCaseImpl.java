package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.ports.RejectInvitationUseCase;
import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RejectInvitationUseCaseImpl implements RejectInvitationUseCase {

    private final InvitationRepositorySPI invitationRepository;

    @Autowired
    public RejectInvitationUseCaseImpl(InvitationRepositorySPI invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public void execute(UUID invitationId, UUID studentId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitación no encontrada."));

        if (!invitation.getInvitedStudentId().equals(studentId)) {
            throw new ActionNotAllowedException("No puedes rechazar una invitación dirigida a otro estudiante.");
        }

        invitation.reject();
        invitationRepository.save(invitation);
    }
}

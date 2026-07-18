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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AcceptInvitationUseCaseImpl implements AcceptInvitationUseCase {

    private final InvitationRepositorySPI invitationRepository;
    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public AcceptInvitationUseCaseImpl(InvitationRepositorySPI invitationRepository, ParcheRepositorySPI parcheRepository) {
        this.invitationRepository = invitationRepository;
        this.parcheRepository = parcheRepository;
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
    }
}

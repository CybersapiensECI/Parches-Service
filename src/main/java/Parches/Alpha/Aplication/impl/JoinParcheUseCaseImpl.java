package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.JoinParcheUseCase;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Enums.ParcheType;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import Parches.Alpha.Infrastructure.output.messaging.ParcheEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class JoinParcheUseCaseImpl implements JoinParcheUseCase {

    private final ParcheRepositorySPI parcheRepository;
    private final InvitationRepositorySPI invitationRepository;
    private final ParcheEventPublisher eventPublisher;

    @Autowired
    public JoinParcheUseCaseImpl(ParcheRepositorySPI parcheRepository, InvitationRepositorySPI invitationRepository,
                                 ParcheEventPublisher eventPublisher) {
        this.parcheRepository = parcheRepository;
        this.invitationRepository = invitationRepository;
        this.eventPublisher = eventPublisher;
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
        eventPublisher.publishMemberJoined(parcheId, studentId);
    }
}

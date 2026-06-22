package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.SendInvitationCommand;
import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.DuplicateInvitationException;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendInvitationUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @Mock
    private InvitationRepositorySPI invitationRepository;

    @InjectMocks
    private SendInvitationUseCaseImpl sendInvitationUseCase;

    private UUID parcheId;
    private UUID senderId;
    private UUID invitedId;
    private Parche parche;

    @BeforeEach
    void setUp() {
        parcheId = UUID.randomUUID();
        senderId = UUID.randomUUID();
        invitedId = UUID.randomUUID();
        parche = Parche.builder()
                .id(parcheId)
                .creatorStudentId(senderId)
                .maximumQuota(10)
                .status(Parches.Alpha.Domain.Enums.ParcheStatus.ACTIVE)
                .members(new ArrayList<>())
                .invitations(new ArrayList<>())
                .build();
    }

    @Test
    void testSendInvitationSuccess() {
        parche.addMember(senderId, MemberRole.CREATOR);

        SendInvitationCommand command = new SendInvitationCommand(parcheId, senderId, invitedId);

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));
        when(invitationRepository.existsPendingInvitation(parcheId, invitedId)).thenReturn(false);

        UUID result = sendInvitationUseCase.execute(command);

        assertNotNull(result);
        verify(invitationRepository).save(any(Invitation.class));
        verify(parcheRepository).save(parche);
    }

    @Test
    void testSendInvitationNotMember() {
        SendInvitationCommand command = new SendInvitationCommand(parcheId, senderId, invitedId);
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        assertThrows(ActionNotAllowedException.class, () ->
                sendInvitationUseCase.execute(command)
        );
    }

    @Test
    void testSendInvitationDuplicate() {
        parche.addMember(senderId, MemberRole.CREATOR);
        SendInvitationCommand command = new SendInvitationCommand(parcheId, senderId, invitedId);

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));
        when(invitationRepository.existsPendingInvitation(parcheId, invitedId)).thenReturn(true);

        assertThrows(DuplicateInvitationException.class, () ->
                sendInvitationUseCase.execute(command)
        );
    }
}

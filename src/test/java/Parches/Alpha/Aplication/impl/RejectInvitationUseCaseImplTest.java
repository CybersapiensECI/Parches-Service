package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.State.PendingState;
import Parches.Alpha.Domain.State.RejectedState;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RejectInvitationUseCaseImplTest {

    @Mock
    private InvitationRepositorySPI invitationRepository;

    @InjectMocks
    private RejectInvitationUseCaseImpl rejectInvitationUseCase;

    private UUID invitationId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        invitationId = UUID.randomUUID();
        studentId = UUID.randomUUID();
    }

    @Test
    void testRejectInvitationSuccess() {
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(UUID.randomUUID())
                .inviterId(UUID.randomUUID())
                .invitedStudentId(studentId)
                .state(new PendingState())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        rejectInvitationUseCase.execute(invitationId, studentId);

        assertTrue(invitation.getState() instanceof RejectedState);
        verify(invitationRepository).save(invitation);
    }

    @Test
    void testRejectInvitationNotAllowed() {
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(UUID.randomUUID())
                .inviterId(UUID.randomUUID())
                .invitedStudentId(UUID.randomUUID())
                .state(new PendingState())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        assertThrows(ActionNotAllowedException.class, () ->
                rejectInvitationUseCase.execute(invitationId, studentId)
        );
        verify(invitationRepository, never()).save(any());
    }
}

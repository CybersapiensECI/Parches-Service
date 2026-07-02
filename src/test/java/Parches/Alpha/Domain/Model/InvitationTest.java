package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.State.AcceptedState;
import Parches.Alpha.Domain.State.PendingState;
import Parches.Alpha.Domain.State.RejectedState;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InvitationTest {

    @Test
    void testInvitationBuilderValidation() {
        assertThrows(RuntimeException.class, () ->
                Invitation.builder()
                        .inviterId(UUID.randomUUID())
                        .invitedStudentId(UUID.randomUUID())
                        .build()
        );

        assertThrows(RuntimeException.class, () ->
                Invitation.builder()
                        .parcheId(UUID.randomUUID())
                        .invitedStudentId(UUID.randomUUID())
                        .build()
        );

        assertThrows(RuntimeException.class, () ->
                Invitation.builder()
                        .parcheId(UUID.randomUUID())
                        .inviterId(UUID.randomUUID())
                        .build()
        );
    }

    @Test
    void testAcceptInvitationTransitionsState() {
        Invitation invitation = Invitation.builder()
                .parcheId(UUID.randomUUID())
                .inviterId(UUID.randomUUID())
                .invitedStudentId(UUID.randomUUID())
                .state(new PendingState())
                .build();

        assertTrue(invitation.getState() instanceof PendingState);

        invitation.accept();
        assertTrue(invitation.getState() instanceof AcceptedState);
        assertNotNull(invitation.getRespondedAt());
    }

    @Test
    void testRejectInvitationTransitionsState() {
        Invitation invitation = Invitation.builder()
                .parcheId(UUID.randomUUID())
                .inviterId(UUID.randomUUID())
                .invitedStudentId(UUID.randomUUID())
                .state(new PendingState())
                .build();

        invitation.reject();
        assertTrue(invitation.getState() instanceof RejectedState);
        assertNotNull(invitation.getRespondedAt());
    }
}

package Parches.Alpha.Domain.State;

import Parches.Alpha.Domain.Model.Invitation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InvitationStateTest {

    private Invitation invitation;

    @BeforeEach
    void setUp() {
        invitation = Invitation.builder()
                .parcheId(UUID.randomUUID())
                .inviterId(UUID.randomUUID())
                .invitedStudentId(UUID.randomUUID())
                .build();
    }

    @Test
    void testPendingState() {
        PendingState state = new PendingState();
        assertEquals("PENDING", state.getStatusName());

        state.accept(invitation);
        assertTrue(invitation.getState() instanceof AcceptedState);

        invitation.changeState(new PendingState());
        state.reject(invitation);
        assertTrue(invitation.getState() instanceof RejectedState);
    }

    @Test
    void testAcceptedState() {
        AcceptedState state = new AcceptedState();
        assertEquals("ACCEPTED", state.getStatusName());

        assertThrows(RuntimeException.class, () -> state.accept(invitation));
        assertThrows(RuntimeException.class, () -> state.reject(invitation));
    }

    @Test
    void testRejectedState() {
        RejectedState state = new RejectedState();
        assertEquals("REJECTED", state.getStatusName());

        assertThrows(RuntimeException.class, () -> state.accept(invitation));
        assertThrows(RuntimeException.class, () -> state.reject(invitation));
    }
}

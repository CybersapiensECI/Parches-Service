package Parches.Alpha.Domain.State;

import Parches.Alpha.Domain.Model.Invitation;

public class RejectedState implements InvitationState {

    @Override
    public void accept(Invitation invitation) {
        throw new RuntimeException("Una invitación rechazada no puede volver a ser aceptada.");
    }

    @Override
    public void reject(Invitation invitation) {
        throw new RuntimeException("La invitación ya ha sido rechazada previamente.");
    }

    @Override
    public String getStatusName() {
        return "REJECTED";
    }
}

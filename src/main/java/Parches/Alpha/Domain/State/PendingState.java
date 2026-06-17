package Parches.Alpha.Domain.State;

import Parches.Alpha.Domain.Model.Invitation;

public class PendingState implements InvitationState {

    @Override
    public void accept(Invitation invitation) {
        // Ejecuta la transición mutando el estado interno de la invitación
        invitation.changeState(new AcceptedState());
    }

    @Override
    public void reject(Invitation invitation) {
        invitation.changeState(new RejectedState());
    }

    @Override
    public String getStatusName() {
        return "PENDING";
    }
}

package Parches.Alpha.Domain.State;

import Parches.Alpha.Domain.Model.Invitation;

public class AcceptedState implements InvitationState {

    @Override
    public void accept(Invitation invitation) {
        throw new RuntimeException("La invitación ya ha sido aceptada previamente y no puede cambiar su estado.");
    }

    @Override
    public void reject(Invitation invitation) {
        throw new RuntimeException("Una invitación ya aceptada no puede ser marcada como rechazada.");
    }

    @Override
    public String getStatusName() {
        return "ACCEPTED";
    }
}

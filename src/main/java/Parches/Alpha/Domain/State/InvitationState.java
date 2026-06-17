package Parches.Alpha.Domain.State;

import Parches.Alpha.Domain.Model.Invitation;

public interface InvitationState {
    void accept(Invitation invitation);
    void reject(Invitation invitation);
    String getStatusName();
}

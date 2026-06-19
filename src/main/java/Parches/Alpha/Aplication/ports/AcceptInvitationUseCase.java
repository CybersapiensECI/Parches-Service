package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface AcceptInvitationUseCase {
    void execute(UUID invitationId, UUID studentId);
}

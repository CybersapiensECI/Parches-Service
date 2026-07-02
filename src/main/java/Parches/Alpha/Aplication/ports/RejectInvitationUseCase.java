package Parches.Alpha.Aplication.ports;

import java.util.UUID;

public interface RejectInvitationUseCase {
    void execute(UUID invitationId, UUID studentId);
}

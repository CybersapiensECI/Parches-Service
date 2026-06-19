package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Aplication.dto.SendInvitationCommand;

import java.util.UUID;

public interface SendInvitationUseCase {
    UUID execute(SendInvitationCommand command);
}

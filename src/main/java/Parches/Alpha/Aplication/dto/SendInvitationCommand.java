package Parches.Alpha.Aplication.dto;

import java.util.UUID;

public record SendInvitationCommand(
        UUID parcheId,
        UUID senderId,      // quien envía la invitación
        UUID invitedId      // El estudiante invitado
) {}

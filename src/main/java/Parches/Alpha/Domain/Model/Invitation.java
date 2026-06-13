package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Invitation {
    private final UUID id;
    private final UUID parcheId;
    private final UUID senderId;
    private final UUID invitedId;
    // private InvitationState state;
    private final LocalDateTime sendAt;
    private LocalDateTime respondedAt;
}

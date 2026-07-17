package Parches.Alpha.Infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** chat-service la consume para agregar el miembro a la sala grupal (routing key parche.member-joined). */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcheMemberJoinedMessage {
    private UUID parcheId;
    private UUID studentId;
}

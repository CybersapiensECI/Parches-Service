package Parches.Alpha.Infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** chat-service la consume para crear la sala grupal (routing key parche.created). */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcheCreatedMessage {
    private UUID parcheId;
    private UUID creatorId;
}

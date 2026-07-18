package Parches.Alpha.Infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * chat-service la consume para agregar el miembro a la sala grupal
 * (routing key parche.member-joined). GamificationService también la
 * consume para las monas ANFITRION (necesita creatorId, antes ausente:
 * nadie podía acreditar la actividad al dueño del parche) y ATLETA_PATIO
 * (necesita category, antes ausente: isSportsRelated quedaba fijo en
 * false sin importar el parche).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcheMemberJoinedMessage {
    private UUID parcheId;
    private UUID studentId;
    private UUID creatorId;
    private String category;
}

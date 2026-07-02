package Parches.Alpha.Infrastructure.output.db.mapper;

import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.State.*;
import Parches.Alpha.Infrastructure.output.db.entity.InvitationEntity;

public class InvitationMapper {

    public static Invitation toDomain(InvitationEntity entity) {
        if (entity == null) return null;

        InvitationState state;
        String stateStr = entity.getState();
        if ("ACCEPTED".equalsIgnoreCase(stateStr)) {
            state = new AcceptedState();
        } else if ("REJECTED".equalsIgnoreCase(stateStr)) {
            state = new RejectedState();
        } else {
            state = new PendingState();
        }

        return Invitation.builder()
                .id(entity.getId())
                .parcheId(entity.getParcheId())
                .inviterId(entity.getSenderId())
                .invitedStudentId(entity.getInvitedId())
                .state(state)
                .sentAt(entity.getSendAt())
                .respondedAt(entity.getRespondedAt())
                .build();
    }

    public static InvitationEntity toEntity(Invitation domain) {
        if (domain == null) return null;

        return InvitationEntity.builder()
                .id(domain.getId())
                .parcheId(domain.getParcheId())
                .senderId(domain.getInviterId())
                .invitedId(domain.getInvitedStudentId())
                .state(domain.getState().getStatusName())
                .sendAt(domain.getSentAt())
                .respondedAt(domain.getRespondedAt())
                .build();
    }
}

package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.State.InvitationState;
import Parches.Alpha.Domain.State.PendingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Invitation {
    @Builder.Default
    private final UUID id = UUID.randomUUID();
    private final UUID parcheId;
    private final UUID inviterId;
    private final UUID invitedStudentId;
    @Builder.Default
    private InvitationState state = new PendingState();
    @Builder.Default
    private final LocalDateTime sentAt = LocalDateTime.now();
    private LocalDateTime respondedAt;

    public static class InvitationBuilder {
        public Invitation build() {
            Optional.ofNullable(this.parcheId)
                    .orElseThrow(() -> new RuntimeException("El id del parche es obligatorio."));
            Optional.ofNullable(this.inviterId)
                    .orElseThrow(() -> new RuntimeException("El id del invitador es obligatorio."));
            Optional.ofNullable(this.invitedStudentId)
                    .orElseThrow(() -> new RuntimeException("El id del estudiante invitado es obligatorio."));

            return new Invitation(
                    this.id$set ? this.id$value : UUID.randomUUID(),
                    this.parcheId,
                    this.inviterId,
                    this.invitedStudentId,
                    this.state$set ? this.state$value : new PendingState(),
                    this.sentAt$set ? this.sentAt$value : LocalDateTime.now(),
                    this.respondedAt);
        }
    }

    public void changeState(InvitationState newState) {
        this.state = newState;
        this.respondedAt = LocalDateTime.now();
    }

    public void accept() {
        this.state.accept(this);
    }

    public void reject() {
        this.state.reject(this);
    }
}
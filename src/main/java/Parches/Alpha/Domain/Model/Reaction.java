package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Reaction {
    @Builder.Default
    private final UUID id = UUID.randomUUID();
    private final UUID studentId;
    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();

    public static class ReactionBuilder {
        public Reaction build() {
            Optional.ofNullable(this.studentId).orElseThrow(() -> new RuntimeException("El id del estudiante que reacciona no puede ser nulo."));

            return new Reaction(
                    this.id$set ? this.id$value : UUID.randomUUID(),
                    this.studentId,
                    this.createdAt$set ? this.createdAt$value : LocalDateTime.now()
            );
        }
    }
}
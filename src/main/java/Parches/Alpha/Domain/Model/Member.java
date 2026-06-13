package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Member {
    private final UUID id;
    private final UUID parcheId;
    private final UUID student;
    private final LocalDateTime unionDate;
    private final MemberRole role;

    public static class MemberBuilder {
        public Member build() {
            Optional.ofNullable(this.student)
                    .orElseThrow(() -> new RuntimeException("El identificador del estudiante miembro no puede ser nulo."));

            Optional.ofNullable(this.parcheId)
                    .orElseThrow(() -> new RuntimeException("El miembro debe estar asociado a un parche válido."));

            UUID finalId = Optional.ofNullable(this.id)
                    .orElseGet(() -> UUID.randomUUID());

            MemberRole finalRole = Optional.ofNullable(this.role)
                    .orElseGet(() -> MemberRole.STUDENT);

            LocalDateTime finalUnionDate = Optional.ofNullable(this.unionDate)
                    .orElseGet(() -> LocalDateTime.now());

            return new Member(finalId, this.parcheId, this.student, finalUnionDate, finalRole);
        }
    }
}
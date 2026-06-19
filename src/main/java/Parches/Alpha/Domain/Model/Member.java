package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.Enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private UUID id;
    private UUID parcheId;
    private UUID studentId;
    private LocalDateTime unionDate;
    private MemberRole role;

    /**
     * Valida e inicializa los campos usando flujos funcionales sin un solo 'if'.
     */
    public void validateAndInitialize() {
        this.studentId = Optional.ofNullable(this.studentId)
                .orElseThrow(() -> new RuntimeException("El identificador del estudiante miembro no puede ser nulo."));

        this.parcheId = Optional.ofNullable(this.parcheId)
                .orElseThrow(() -> new RuntimeException("El miembro debe estar asociado a un parche válido."));

        this.id = Optional.ofNullable(this.id).orElseGet(UUID::randomUUID);
        this.role = Optional.ofNullable(this.role).orElse(MemberRole.STUDENT);
        this.unionDate = Optional.ofNullable(this.unionDate).orElseGet(LocalDateTime::now);
    }
}
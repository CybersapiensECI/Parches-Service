package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID studentId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Valida de forma estricta que la reacción cumpla con las reglas de negocio básicas.
     */
    public void validate() {
        Objects.requireNonNull(this.studentId, "El id del estudiante que reacciona no puede ser nulo.");
    }
}
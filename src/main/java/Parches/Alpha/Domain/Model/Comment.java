package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private UUID authorId;
    private String text;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private List<Reaction> reactions = new ArrayList<>();

    /**
     * Valida de forma funcional que el comentario cumpla con las reglas de negocio.
     */
    public void validate() {
        Optional.ofNullable(this.authorId)
                .orElseThrow(() -> new RuntimeException("El id del autor del comentario no puede ser nulo."));

        Optional.ofNullable(this.text)
                .filter(t -> !t.trim().isEmpty())
                .orElseThrow(() -> new RuntimeException("El texto del comentario no puede estar vacío."));
    }

    /**
     * Enciende o apaga la reacción de un estudiante en este comentario.
     */
    public void toggleReaction(UUID studentId) {
        Optional.ofNullable(studentId)
                .orElseThrow(() -> new RuntimeException("El id del estudiante que reacciona no puede ser nulo."));

        this.reactions = Optional.ofNullable(this.reactions).orElseGet(ArrayList::new);

        this.reactions.stream()
                .filter(r -> studentId.equals(r.getStudentId()))
                .findFirst()
                .ifPresentOrElse(
                        this.reactions::remove,
                        () -> this.reactions.add(Reaction.builder().studentId(studentId).build())
                );
    }
}
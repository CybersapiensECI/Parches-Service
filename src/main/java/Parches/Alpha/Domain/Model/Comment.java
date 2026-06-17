package Parches.Alpha.Domain.Model;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Comment {
    private final UUID id;
    private final UUID authorId;
    private String text;
    private final LocalDateTime createdAt;
    private final List<Reaction> reactions;

    @Builder
    public Comment(UUID id, UUID authorId, String text) {
        Optional.ofNullable(authorId)
                .orElseThrow(() -> new RuntimeException("El id del autor del comentario no puede ser nulo."));

        Optional.ofNullable(text)
                .filter(t -> !t.trim().isEmpty())
                .orElseThrow(() -> new RuntimeException("El texto del comentario no puede estar vacío."));

        this.id = id != null ? id : UUID.randomUUID();
        this.authorId = authorId;
        this.text = text;
        this.createdAt = LocalDateTime.now();
        this.reactions = new ArrayList<>();
    }

    /**
     * Enciende o apaga la reacción (corazón) de un estudiante en este comentario.
     */
    public void toggleReaction(UUID studentId) {
        Optional.ofNullable(studentId)
                .orElseThrow(() -> new RuntimeException("El id del estudiante que reacciona no puede ser nulo."));

        Optional<Reaction> existingReaction = this.reactions.stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .findFirst();

        existingReaction.ifPresentOrElse(
                this.reactions::remove,
                () -> this.reactions.add(Reaction.builder().studentId(studentId).build())
        );
    }

    public List<Reaction> getReactions() {
        return Collections.unmodifiableList(reactions);
    }
}
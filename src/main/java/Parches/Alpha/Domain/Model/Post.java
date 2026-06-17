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
@Builder
@AllArgsConstructor
public class Post {
    @Builder.Default
    private final UUID id = UUID.randomUUID();
    private String text;
    private String photoUrl;
    @Builder.Default
    private final LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private final List<Comment> comments = new ArrayList<>();
    @Builder.Default
    private final List<Reaction> reactions = new ArrayList<>();

    public static class PostBuilder {
        public Post build() {
            boolean hasText = Optional.ofNullable(this.text).filter(t -> !t.trim().isEmpty()).isPresent();
            boolean hasPhoto = Optional.ofNullable(this.photoUrl).filter(p -> !p.trim().isEmpty()).isPresent();

            if (!hasText && !hasPhoto) {
                throw new RuntimeException("Una publicación debe contener obligatoriamente texto, una foto o ambos.");
            }

            return new Post(
                    this.id$set ? this.id$value : UUID.randomUUID(),
                    this.text,
                    this.photoUrl,
                    this.createdAt$set ? this.createdAt$value : LocalDateTime.now(),
                    this.comments$set ? this.comments$value : new ArrayList<>(),
                    this.reactions$set ? this.reactions$value : new ArrayList<>()
            );
        }
    }

    public void addComment(UUID authorId, String text) {
        Optional.ofNullable(text)
                .filter(t -> !t.trim().isEmpty())
                .orElseThrow(() -> new RuntimeException("El texto del comentario no puede estar vacío."));

        this.comments.add(Comment.builder().authorId(authorId).text(text).build());
    }

    public void toggleReaction(UUID studentId) {
        Optional<Reaction> existingReaction = this.reactions.stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .findFirst();

        existingReaction.ifPresentOrElse(
                this.reactions::remove,
                () -> this.reactions.add(Reaction.builder().studentId(studentId).build())
        );
    }

    public void toggleCommentReaction(UUID studentId, UUID commentId) {
        this.comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El comentario especificado no existe en esta publicación."))
                .toggleReaction(studentId);
    }

    public List<Comment> getComments() { return Collections.unmodifiableList(comments); }
    public List<Reaction> getReactions() { return Collections.unmodifiableList(reactions); }
}
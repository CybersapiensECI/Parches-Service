package Parches.Alpha.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String text;
    private String photoUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    private List<Reaction> reactions = new ArrayList<>();
    private UUID authorId;

    /**
     * Valida de forma funcional que la publicación contenga texto o foto.
     */
    public void validate() {
        boolean hasText = Optional.ofNullable(this.text).filter(t -> !t.trim().isEmpty()).isPresent();
        boolean hasPhoto = Optional.ofNullable(this.photoUrl).filter(p -> !p.trim().isEmpty()).isPresent();

        if (!hasText && !hasPhoto) {
            throw new RuntimeException("Una publicación debe contener obligatoriamente texto, una foto o ambos.");
        }
    }

    public void addComment(UUID authorId, String text) {
        Comment newComment = Comment.builder().authorId(authorId).text(text).build();
        newComment.validate();

        this.comments = Optional.ofNullable(this.comments).orElseGet(ArrayList::new);
        this.comments.add(newComment);
    }

    public void toggleReaction(UUID studentId) {
        this.reactions = Optional.ofNullable(this.reactions).orElseGet(ArrayList::new);

        this.reactions.stream()
                .filter(r -> studentId.equals(r.getStudentId()))
                .findFirst()
                .ifPresentOrElse(
                        this.reactions::remove,
                        () -> {
                            Reaction newReaction = Reaction.builder().studentId(studentId).build();
                            newReaction.validate();
                            this.reactions.add(newReaction);
                        }
                );
    }

    public void toggleCommentReaction(UUID studentId, UUID commentId) {
        Optional.ofNullable(this.comments)
                .orElseGet(ArrayList::new)
                .stream()
                .filter(c -> commentId.equals(c.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El comentario especificado no existe en esta publicación."))
                .toggleReaction(studentId);
    }

    // Vistas seguras expuestas al exterior
    public List<Comment> getUnmodifiableComments() {
        return Optional.ofNullable(this.comments).map(Collections::unmodifiableList).orElse(List.of());
    }

    public List<Reaction> getUnmodifiableReactions() {
        return Optional.ofNullable(this.reactions).map(Collections::unmodifiableList).orElse(List.of());
    }
}
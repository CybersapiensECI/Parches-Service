package Parches.Alpha.Infrastructure.output.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PostPersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }

    @Column(length = 1000)
    private String text;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parche_id", nullable = false)
    private ParcheEntity parche;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReactionEntity> reactions;
}

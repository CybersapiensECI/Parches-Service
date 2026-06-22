package Parches.Alpha.Infrastructure.output.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity implements Persistable<UUID> {

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

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "union_date", nullable = false)
    private LocalDateTime unionDate;

    @Column(name = "role", nullable = false, length = 20)
    private String role; // STUDENT / CREATOR

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parche_id", nullable = false)
    private ParcheEntity parche;
}

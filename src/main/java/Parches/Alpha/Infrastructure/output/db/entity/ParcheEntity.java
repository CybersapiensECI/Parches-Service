package Parches.Alpha.Infrastructure.output.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "parches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcheEntity implements Persistable<UUID> {

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

    @NotNull
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "lugar", length = 50)
    private String lugar; // Mapea tu Enum de lugares mapeado a String en base de datos

    private String category;

    @Column(nullable = false, length = 20)
    private String type; // Lo manejamos como String para acoplarlo con el Mapper y evitar conflictos de tipos directos

    @Column(nullable = false, length = 20)
    private String status; // ACTIVE / FILED

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "hora", nullable = false)
    private LocalTime hour;

    @Min(1) @Max(30)
    @Column(name = "maximum_quota", nullable = false)
    private int maximumQuota;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private UUID creatorStudentId;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Relación unida en cascada con los miembros del parche (1:N)
    @OneToMany(mappedBy = "parche", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberEntity> members;

    // Relación unida en cascada con los posts del parche (1:N)
    @OneToMany(mappedBy = "parche", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostEntity> posts;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }
}

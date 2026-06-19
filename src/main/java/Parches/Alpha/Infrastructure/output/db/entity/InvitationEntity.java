package Parches.Alpha.Infrastructure.output.db.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "parche_id", nullable = false)
    private UUID parcheId;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Column(name = "invited_id", nullable = false)
    private UUID invitedId;

    @Column(nullable = false, length = 20)
    private String state; // PENDING / ACCEPTED / REJECTED

    @Column(name = "send_at", nullable = false, updatable = false)
    private LocalDateTime sendAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @PrePersist
    public void prePersist() {
        this.sendAt = LocalDateTime.now();
    }
}
package Parches.Alpha.Infrastructure.output.db.mapper;

import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.State.AcceptedState;
import Parches.Alpha.Domain.State.PendingState;
import Parches.Alpha.Domain.State.RejectedState;
import Parches.Alpha.Infrastructure.output.db.entity.InvitationEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InvitationMapperTest {

    @Test
    void testToDomainNull() {
        assertNull(InvitationMapper.toDomain(null));
    }

    @Test
    void testToEntityNull() {
        assertNull(InvitationMapper.toEntity(null));
    }

    @Test
    void testToDomainAndEntityAcceptedState() {
        UUID invitationId = UUID.randomUUID();
        UUID parcheId = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        InvitationEntity entity = InvitationEntity.builder()
                .id(invitationId)
                .parcheId(parcheId)
                .senderId(senderId)
                .invitedId(invitedId)
                .state("ACCEPTED")
                .sendAt(now)
                .respondedAt(now)
                .build();

        Invitation domain = InvitationMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(invitationId, domain.getId());
        assertTrue(domain.getState() instanceof AcceptedState);
        assertEquals(parcheId, domain.getParcheId());
        assertEquals(senderId, domain.getInviterId());
        assertEquals(invitedId, domain.getInvitedStudentId());

        InvitationEntity mappedBack = InvitationMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("ACCEPTED", mappedBack.getState());
    }

    @Test
    void testToDomainAndEntityRejectedState() {
        UUID invitationId = UUID.randomUUID();
        InvitationEntity entity = InvitationEntity.builder()
                .id(invitationId)
                .parcheId(UUID.randomUUID())
                .senderId(UUID.randomUUID())
                .invitedId(UUID.randomUUID())
                .state("REJECTED")
                .sendAt(LocalDateTime.now())
                .build();

        Invitation domain = InvitationMapper.toDomain(entity);

        assertNotNull(domain);
        assertTrue(domain.getState() instanceof RejectedState);

        InvitationEntity mappedBack = InvitationMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("REJECTED", mappedBack.getState());
    }

    @Test
    void testToDomainAndEntityPendingState() {
        UUID invitationId = UUID.randomUUID();
        InvitationEntity entity = InvitationEntity.builder()
                .id(invitationId)
                .parcheId(UUID.randomUUID())
                .senderId(UUID.randomUUID())
                .invitedId(UUID.randomUUID())
                .state("PENDING")
                .sendAt(LocalDateTime.now())
                .build();

        Invitation domain = InvitationMapper.toDomain(entity);

        assertNotNull(domain);
        assertTrue(domain.getState() instanceof PendingState);

        InvitationEntity mappedBack = InvitationMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("PENDING", mappedBack.getState());
    }

    @Test
    void testToDomainFallbackState() {
        UUID invitationId = UUID.randomUUID();
        InvitationEntity entity = InvitationEntity.builder()
                .id(invitationId)
                .parcheId(UUID.randomUUID())
                .senderId(UUID.randomUUID())
                .invitedId(UUID.randomUUID())
                .state("UNKNOWN")
                .sendAt(LocalDateTime.now())
                .build();

        Invitation domain = InvitationMapper.toDomain(entity);

        assertNotNull(domain);
        assertTrue(domain.getState() instanceof PendingState);
    }
}

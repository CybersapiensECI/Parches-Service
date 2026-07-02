package Parches.Alpha.Infrastructure.output.db.adapter;

import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.State.PendingState;
import Parches.Alpha.Infrastructure.output.db.entity.InvitationEntity;
import Parches.Alpha.Infrastructure.output.db.repository.InvitationJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationDatabaseAdapterTest {

    @Mock
    private InvitationJpaRepository invitationJpaRepository;

    @InjectMocks
    private InvitationDatabaseAdapter invitationDatabaseAdapter;

    private UUID invitationId;
    private Invitation invitationDomain;
    private InvitationEntity invitationEntity;

    @BeforeEach
    void setUp() {
        invitationId = UUID.randomUUID();
        invitationDomain = Invitation.builder()
                .id(invitationId)
                .parcheId(UUID.randomUUID())
                .inviterId(UUID.randomUUID())
                .invitedStudentId(UUID.randomUUID())
                .state(new PendingState())
                .sentAt(LocalDateTime.now())
                .build();
        invitationEntity = InvitationEntity.builder()
                .id(invitationId)
                .parcheId(invitationDomain.getParcheId())
                .senderId(invitationDomain.getInviterId())
                .invitedId(invitationDomain.getInvitedStudentId())
                .state("PENDING")
                .sendAt(invitationDomain.getSentAt())
                .build();
    }

    @Test
    void testSaveNewInvitation() {
        when(invitationJpaRepository.existsById(invitationId)).thenReturn(false);
        when(invitationJpaRepository.save(any(InvitationEntity.class))).thenReturn(invitationEntity);

        UUID result = invitationDatabaseAdapter.save(invitationDomain);

        assertEquals(invitationId, result);
        verify(invitationJpaRepository).save(any(InvitationEntity.class));
    }

    @Test
    void testSaveExistingInvitation() {
        when(invitationJpaRepository.existsById(invitationId)).thenReturn(true);
        when(invitationJpaRepository.save(any(InvitationEntity.class))).thenReturn(invitationEntity);

        UUID result = invitationDatabaseAdapter.save(invitationDomain);

        assertEquals(invitationId, result);
        verify(invitationJpaRepository).save(any(InvitationEntity.class));
    }

    @Test
    void testFindById() {
        when(invitationJpaRepository.findById(invitationId)).thenReturn(Optional.of(invitationEntity));

        Optional<Invitation> result = invitationDatabaseAdapter.findById(invitationId);

        assertTrue(result.isPresent());
        assertEquals(invitationId, result.get().getId());
    }

    @Test
    void testExistsPendingInvitation() {
        UUID parcheId = UUID.randomUUID();
        UUID invitedId = UUID.randomUUID();
        when(invitationJpaRepository.existsByParcheIdAndInvitedIdAndStateIgnoreCase(parcheId, invitedId, "PENDING"))
                .thenReturn(true);

        boolean result = invitationDatabaseAdapter.existsPendingInvitation(parcheId, invitedId);

        assertTrue(result);
    }
}

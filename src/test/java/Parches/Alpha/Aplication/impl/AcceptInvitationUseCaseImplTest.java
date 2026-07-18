package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Model.Invitation;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.State.AcceptedState;
import Parches.Alpha.Domain.State.PendingState;
import Parches.Alpha.Domain.spi.InvitationRepositorySPI;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptInvitationUseCaseImplTest {

    @Mock
    private InvitationRepositorySPI invitationRepository;

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @Mock
    private Parches.Alpha.Infrastructure.output.messaging.ParcheEventPublisher eventPublisher;

    @InjectMocks
    private AcceptInvitationUseCaseImpl acceptInvitationUseCase;

    private UUID invitationId;
    private UUID studentId;
    private UUID parcheId;

    @BeforeEach
    void setUp() {
        invitationId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        parcheId = UUID.randomUUID();
    }

    @Test
    void testAcceptInvitationSuccess() {
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(parcheId)
                .inviterId(UUID.randomUUID())
                .invitedStudentId(studentId)
                .state(new PendingState())
                .build();

        Parche parche = Parche.builder()
                .id(parcheId)
                .status(ParcheStatus.ACTIVE)
                .maximumQuota(5)
                .members(new ArrayList<>())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        acceptInvitationUseCase.execute(invitationId, studentId);

        assertTrue(invitation.getState() instanceof AcceptedState);
        assertTrue(parche.isStudentMember(studentId));
        verify(invitationRepository).save(invitation);
        verify(parcheRepository).save(parche);
    }

    @Test
    void testAcceptInvitationNotAllowedForOtherStudent() {
        UUID differentStudentId = UUID.randomUUID();
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(parcheId)
                .inviterId(UUID.randomUUID())
                .invitedStudentId(differentStudentId)
                .state(new PendingState())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class, () ->
                acceptInvitationUseCase.execute(invitationId, studentId)
        );

        assertEquals("No puedes aceptar una invitación dirigida a otro estudiante.", exception.getMessage());
        verify(parcheRepository, never()).findById(any());
        verify(invitationRepository, never()).save(any());
        verify(parcheRepository, never()).save(any());
    }

    @Test
    void testAcceptInvitationParcheFull() {
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(parcheId)
                .inviterId(UUID.randomUUID())
                .invitedStudentId(studentId)
                .state(new PendingState())
                .build();

        Parche parche = Parche.builder()
                .id(parcheId)
                .status(ParcheStatus.ACTIVE)
                .maximumQuota(0)
                .members(new ArrayList<>())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        ParcheFullException exception = assertThrows(ParcheFullException.class, () ->
                acceptInvitationUseCase.execute(invitationId, studentId)
        );

        assertEquals("No hay cupo disponible en el parche para aceptar la invitación.", exception.getMessage());
        verify(invitationRepository, never()).save(any());
        verify(parcheRepository, never()).save(any());
    }

    @Test
    void testAcceptInvitationParcheNotFound() {
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(parcheId)
                .inviterId(UUID.randomUUID())
                .invitedStudentId(studentId)
                .state(new PendingState())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                acceptInvitationUseCase.execute(invitationId, studentId)
        );

        assertEquals("El parche asociado a esta invitación no existe.", exception.getMessage());
        verify(invitationRepository, never()).save(any());
        verify(parcheRepository, never()).save(any());
    }

    @Test
    void testAcceptInvitationParcheInactive() {
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .parcheId(parcheId)
                .inviterId(UUID.randomUUID())
                .invitedStudentId(studentId)
                .state(new PendingState())
                .build();

        Parche parche = Parche.builder()
                .id(parcheId)
                .status(ParcheStatus.FILED)
                .maximumQuota(5)
                .members(new ArrayList<>())
                .build();

        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                acceptInvitationUseCase.execute(invitationId, studentId)
        );

        assertEquals("El parche al que fuiste invitado ya no está activo o ha sido archivado.", exception.getMessage());
        verify(invitationRepository, never()).save(any());
        verify(parcheRepository, never()).save(any());
    }

    @Test
    void testAcceptInvitationNotFound() {
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                acceptInvitationUseCase.execute(invitationId, studentId)
        );

        assertEquals("Invitación no encontrada.", exception.getMessage());
        verify(parcheRepository, never()).findById(any());
        verify(invitationRepository, never()).save(any());
        verify(parcheRepository, never()).save(any());
    }
}

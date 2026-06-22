package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Model.Member;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveParcheUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @InjectMocks
    private LeaveParcheUseCaseImpl leaveParcheUseCase;

    private UUID parcheId;
    private UUID captainId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        parcheId = UUID.randomUUID();
        captainId = UUID.randomUUID();
        studentId = UUID.randomUUID();
    }

    @Test
    void testLeaveParcheSuccess() {
        List<Member> members = new ArrayList<>();
        members.add(Member.builder()
                .id(UUID.randomUUID())
                .parcheId(parcheId)
                .studentId(captainId)
                .role(MemberRole.CREATOR)
                .unionDate(LocalDateTime.now())
                .build());
        members.add(Member.builder()
                .id(UUID.randomUUID())
                .parcheId(parcheId)
                .studentId(studentId)
                .role(MemberRole.STUDENT)
                .unionDate(LocalDateTime.now())
                .build());

        Parche parche = Parche.builder()
                .id(parcheId)
                .creatorStudentId(captainId)
                .members(members)
                .build();

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        leaveParcheUseCase.execute(parcheId, studentId);

        assertFalse(parche.isStudentMember(studentId));
        assertEquals(1, parche.getMembers().size());
        verify(parcheRepository).save(parche);
    }

    @Test
    void testLeaveParcheCaptainCannotLeave() {
        List<Member> members = new ArrayList<>();
        members.add(Member.builder()
                .id(UUID.randomUUID())
                .parcheId(parcheId)
                .studentId(captainId)
                .role(MemberRole.CREATOR)
                .unionDate(LocalDateTime.now())
                .build());

        Parche parche = Parche.builder()
                .id(parcheId)
                .creatorStudentId(captainId)
                .members(members)
                .build();

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class, () ->
                leaveParcheUseCase.execute(parcheId, captainId)
        );

        assertEquals("El capitán no puede salir del parche sin antes transferir su rol o cerrar el parche.", exception.getMessage());
        verify(parcheRepository, never()).save(any(Parche.class));
    }

    @Test
    void testLeaveParcheParcheNotFound() {
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                leaveParcheUseCase.execute(parcheId, studentId)
        );

        assertEquals("Parche no encontrado.", exception.getMessage());
        verify(parcheRepository, never()).save(any(Parche.class));
    }

    @Test
    void testLeaveParcheStudentNotMember() {
        List<Member> members = new ArrayList<>();
        members.add(Member.builder()
                .id(UUID.randomUUID())
                .parcheId(parcheId)
                .studentId(captainId)
                .role(MemberRole.CREATOR)
                .unionDate(LocalDateTime.now())
                .build());

        Parche parche = Parche.builder()
                .id(parcheId)
                .creatorStudentId(captainId)
                .members(members)
                .build();

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                leaveParcheUseCase.execute(parcheId, studentId)
        );

        assertEquals("El estudiante no es miembro de este parche.", exception.getMessage());
        verify(parcheRepository, never()).save(any(Parche.class));
    }
}

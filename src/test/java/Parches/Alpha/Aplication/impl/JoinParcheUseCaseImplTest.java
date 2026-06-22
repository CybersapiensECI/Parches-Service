package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Domain.Enums.ParcheType;
import Parches.Alpha.Domain.Model.Parche;
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
class JoinParcheUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @Mock
    private InvitationRepositorySPI invitationRepository;

    @InjectMocks
    private JoinParcheUseCaseImpl joinParcheUseCase;

    private UUID parcheId;
    private UUID studentId;
    private Parche parche;

    @BeforeEach
    void setUp() {
        parcheId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        parche = Parche.builder()
                .id(parcheId)
                .maximumQuota(10)
                .type(ParcheType.PUBLIC)
                .members(new ArrayList<>())
                .build();
    }

    @Test
    void testJoinParcheSuccess() {
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        joinParcheUseCase.execute(parcheId, studentId);

        assertTrue(parche.isStudentMember(studentId));
        verify(parcheRepository).save(parche);
    }

    @Test
    void testJoinParchePrivateWithoutInvitation() {
        parche.setType(ParcheType.PRIVATE);
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));
        when(invitationRepository.existsPendingInvitation(parcheId, studentId)).thenReturn(false);

        assertThrows(ActionNotAllowedException.class, () ->
                joinParcheUseCase.execute(parcheId, studentId)
        );
    }
}

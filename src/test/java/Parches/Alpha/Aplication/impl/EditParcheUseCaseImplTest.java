package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.UpdateParcheCommand;
import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditParcheUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @InjectMocks
    private EditParcheUseCaseImpl editParcheUseCase;

    private UUID parcheId;
    private UUID creatorId;
    private Parche parche;

    @BeforeEach
    void setUp() {
        parcheId = UUID.randomUUID();
        creatorId = UUID.randomUUID();
        parche = Parche.builder()
                .id(parcheId)
                .creatorStudentId(creatorId)
                .maximumQuota(10)
                .members(new ArrayList<>())
                .build();
    }

    @Test
    void testEditParcheSuccess() {
        UpdateParcheCommand command = new UpdateParcheCommand(
                "Nuevo Nombre", "Nueva Desc", "BUILDING_C", "CINEMA", "PUBLIC",
                LocalDate.now().plusDays(2), LocalTime.of(18, 0), 15
        );

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        editParcheUseCase.execute(parcheId, command, creatorId);

        assertEquals("Nuevo Nombre", parche.getName());
        assertEquals(15, parche.getMaximumQuota());
        verify(parcheRepository).save(parche);
    }

    @Test
    void testEditParcheNotCreator() {
        UpdateParcheCommand command = new UpdateParcheCommand(
                "Nuevo Nombre", "Nueva Desc", "BUILDING_C", "CINEMA", "PUBLIC",
                LocalDate.now().plusDays(2), LocalTime.of(18, 0), 15
        );

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        assertThrows(ActionNotAllowedException.class, () ->
                editParcheUseCase.execute(parcheId, command, UUID.randomUUID())
        );
    }

    @Test
    void testEditParcheQuotaInvalid() {
        UpdateParcheCommand command = new UpdateParcheCommand(
                "Nuevo Nombre", "Nueva Desc", "BUILDING_C", "CINEMA", "PUBLIC",
                LocalDate.now().plusDays(2), LocalTime.of(18, 0), 1 // Menor a 2
        );

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(parche));

        assertThrows(ParcheFullException.class, () ->
                editParcheUseCase.execute(parcheId, command, creatorId)
        );
    }
}

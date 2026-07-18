package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.exception.MaxActiveParchesException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateParcheUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @Mock
    private Parches.Alpha.Infrastructure.output.messaging.ParcheEventPublisher eventPublisher;

    @InjectMocks
    private CreateParcheUseCaseImpl createParcheUseCase;

    private UUID creatorId;
    private CreateParcheCommand validCommand;

    @BeforeEach
    void setUp() {
        creatorId = UUID.randomUUID();
        validCommand = new CreateParcheCommand(
                "Estudio de Algoritmos",
                "Grupo para estudiar algoritmos y estructuras de datos",
                "BUILDING_B",
                "STUDIES",
                "PUBLIC",
                LocalDate.now().plusDays(1),
                LocalTime.of(14, 30),
                10,
                creatorId,
                null
        );
    }

    @Test
    void testCreateParcheSuccess() {
        UUID expectedParcheId = UUID.randomUUID();
        when(parcheRepository.countActiveParchesByStudentId(creatorId)).thenReturn(2L);
        when(parcheRepository.save(any(Parche.class))).thenReturn(expectedParcheId);

        UUID resultId = createParcheUseCase.execute(validCommand);

        assertEquals(expectedParcheId, resultId);
        
        ArgumentCaptor<Parche> parcheCaptor = ArgumentCaptor.forClass(Parche.class);
        verify(parcheRepository).save(parcheCaptor.capture());
        
        Parche savedParche = parcheCaptor.getValue();
        assertNotNull(savedParche);
        assertEquals("Estudio de Algoritmos", savedParche.getName());
        assertEquals(creatorId, savedParche.getCreatorStudentId());
        assertEquals(ParcheStatus.ACTIVE, savedParche.getStatus());
        assertEquals(1, savedParche.getMembers().size());
        assertEquals(MemberRole.CREATOR, savedParche.getMembers().get(0).getRole());
    }

    @Test
    void testCreateParcheMaxActiveParchesLimitReached() {
        when(parcheRepository.countActiveParchesByStudentId(creatorId)).thenReturn(5L);

        MaxActiveParchesException exception = assertThrows(MaxActiveParchesException.class, () ->
                createParcheUseCase.execute(validCommand)
        );

        assertEquals("El estudiante ya superó el máximo de 5 parches activos.", exception.getMessage());
        verify(parcheRepository, never()).save(any(Parche.class));
    }

    @Test
    void testCreateParcheQuotaTooLow() {
        CreateParcheCommand invalidQuotaCommand = new CreateParcheCommand(
                "Estudio",
                "Desc",
                "BUILDING_B",
                "STUDIES",
                "PUBLIC",
                LocalDate.now().plusDays(1),
                LocalTime.of(14, 30),
                1, // Menor que 2
                creatorId,
                null
        );

        when(parcheRepository.countActiveParchesByStudentId(creatorId)).thenReturn(2L);

        ParcheFullException exception = assertThrows(ParcheFullException.class, () ->
                createParcheUseCase.execute(invalidQuotaCommand)
        );

        assertEquals("Cupo inválido. El cupo debe estar entre 2 y 30.", exception.getMessage());
        verify(parcheRepository, never()).save(any(Parche.class));
    }

    @Test
    void testCreateParcheQuotaTooHigh() {
        CreateParcheCommand invalidQuotaCommand = new CreateParcheCommand(
                "Estudio",
                "Desc",
                "BUILDING_B",
                "STUDIES",
                "PUBLIC",
                LocalDate.now().plusDays(1),
                LocalTime.of(14, 30),
                31, // Mayor que 30
                creatorId,
                null
        );

        when(parcheRepository.countActiveParchesByStudentId(creatorId)).thenReturn(2L);

        ParcheFullException exception = assertThrows(ParcheFullException.class, () ->
                createParcheUseCase.execute(invalidQuotaCommand)
        );

        assertEquals("Cupo inválido. El cupo debe estar entre 2 y 30.", exception.getMessage());
        verify(parcheRepository, never()).save(any(Parche.class));
    }
}

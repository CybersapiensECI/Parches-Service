package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetParcheDetailUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @InjectMocks
    private GetParcheDetailUseCaseImpl getParcheDetailUseCase;

    @Test
    void testGetParcheDetailSuccess() {
        UUID parcheId = UUID.randomUUID();
        Parche expectedParche = Parche.builder().id(parcheId).build();

        when(parcheRepository.findById(parcheId)).thenReturn(Optional.of(expectedParche));

        Parche result = getParcheDetailUseCase.execute(parcheId);

        assertEquals(expectedParche, result);
    }

    @Test
    void testGetParcheDetailNotFound() {
        UUID parcheId = UUID.randomUUID();
        when(parcheRepository.findById(parcheId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                getParcheDetailUseCase.execute(parcheId)
        );
    }
}

package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAvailableParchesUseCaseImplTest {

    @Mock
    private ParcheRepositorySPI parcheRepository;

    @InjectMocks
    private SearchAvailableParchesUseCaseImpl searchAvailableParchesUseCase;

    @Test
    void testSearchAvailableParches() {
        ParcheQueryFilter filter = new ParcheQueryFilter(null, null, null, null, null);
        List<Parche> expectedList = Collections.singletonList(Parche.builder().build());

        when(parcheRepository.findAllWithFilters(filter, 0, 10)).thenReturn(expectedList);

        List<Parche> result = searchAvailableParchesUseCase.execute(filter, 0, 10);

        assertEquals(expectedList, result);
        verify(parcheRepository).findAllWithFilters(filter, 0, 10);
    }
}

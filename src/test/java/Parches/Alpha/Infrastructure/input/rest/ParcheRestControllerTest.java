package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Aplication.ports.CreateParcheUseCase;
import Parches.Alpha.Aplication.ports.JoinParcheUseCase;
import Parches.Alpha.Aplication.ports.SearchAvailableParchesUseCase;
import Parches.Alpha.Domain.Model.Parche;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParcheRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateParcheUseCase createParcheUseCase;

    @Mock
    private SearchAvailableParchesUseCase searchAvailableParchesUseCase;

    @Mock
    private JoinParcheUseCase joinParcheUseCase;

    @InjectMocks
    private ParcheRestController parcheRestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(parcheRestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreateParche() throws Exception {
        UUID expectedParcheId = UUID.randomUUID();
        CreateParcheCommand command = new CreateParcheCommand(
                "Futbol", "Partido", "SOCCER_COURT_1", "SPORTS", "PUBLIC",
                LocalDate.now().plusDays(1), LocalTime.of(15, 0), 10,
                UUID.randomUUID(), null
        );

        when(createParcheUseCase.execute(any(CreateParcheCommand.class))).thenReturn(expectedParcheId);

        mockMvc.perform(post("/api/parches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parcheId").value(expectedParcheId.toString()));

        verify(createParcheUseCase).execute(any(CreateParcheCommand.class));
    }

    @Test
    void testSearchParches() throws Exception {
        Parche parche = Parche.builder()
                .id(UUID.randomUUID())
                .name("Futbol")
                .maximumQuota(10)
                .build();

        when(searchAvailableParchesUseCase.execute(any(ParcheQueryFilter.class), eq(0), eq(10)))
                .thenReturn(Collections.singletonList(parche));

        mockMvc.perform(get("/api/parches")
                        .param("category", "SPORTS")
                        .param("place", "SOCCER_COURT_1")
                        .param("date", LocalDate.now().plusDays(1).toString())
                        .param("availableSlots", "5")
                        .param("query", "Futbol")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Futbol"));

        verify(searchAvailableParchesUseCase).execute(any(ParcheQueryFilter.class), eq(0), eq(10));
    }

    @Test
    void testJoinParche() throws Exception {
        UUID parcheId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        ParcheRestController.JoinRequest request = new ParcheRestController.JoinRequest(studentId);

        mockMvc.perform(post("/api/parches/{parcheId}/join", parcheId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Te has unido exitosamente al parche."));

        verify(joinParcheUseCase).execute(parcheId, studentId);
    }
}

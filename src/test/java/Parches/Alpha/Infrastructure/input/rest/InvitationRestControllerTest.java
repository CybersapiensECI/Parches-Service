package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.dto.SendInvitationCommand;
import Parches.Alpha.Aplication.ports.AcceptInvitationUseCase;
import Parches.Alpha.Aplication.ports.SendInvitationUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvitationRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SendInvitationUseCase sendInvitationUseCase;

    @Mock
    private AcceptInvitationUseCase acceptInvitationUseCase;

    @InjectMocks
    private InvitationRestController invitationRestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(invitationRestController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSendInvitation() throws Exception {
        UUID expectedInvitationId = UUID.randomUUID();
        SendInvitationCommand command = new SendInvitationCommand(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        when(sendInvitationUseCase.execute(any(SendInvitationCommand.class))).thenReturn(expectedInvitationId);

        mockMvc.perform(post("/api/invitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.invitationId").value(expectedInvitationId.toString()));

        verify(sendInvitationUseCase).execute(any(SendInvitationCommand.class));
    }

    @Test
    void testAcceptInvitation() throws Exception {
        UUID invitationId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        InvitationRestController.AcceptRequest request = new InvitationRestController.AcceptRequest(studentId);

        mockMvc.perform(post("/api/invitations/{invitationId}/accept", invitationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invitación aceptada correctamente."));

        verify(acceptInvitationUseCase).execute(invitationId, studentId);
    }
}

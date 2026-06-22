package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.dto.SendInvitationCommand;
import Parches.Alpha.Aplication.ports.AcceptInvitationUseCase;
import Parches.Alpha.Aplication.ports.SendInvitationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationRestController {

    private final SendInvitationUseCase sendInvitationUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;

    @PostMapping
    public ResponseEntity<?> sendInvitation(@RequestBody SendInvitationCommand command) {
        UUID invitationId = sendInvitationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("invitationId", invitationId));
    }

    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID invitationId, @RequestBody AcceptRequest request) {
        acceptInvitationUseCase.execute(invitationId, request.studentId());
        return ResponseEntity.ok(java.util.Map.of("message", "Invitación aceptada correctamente."));
    }

    public record AcceptRequest(UUID studentId) {}
}

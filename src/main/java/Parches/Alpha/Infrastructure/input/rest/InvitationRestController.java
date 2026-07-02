package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.dto.SendInvitationCommand;
import Parches.Alpha.Aplication.ports.AcceptInvitationUseCase;
import Parches.Alpha.Aplication.ports.SendInvitationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
@Tag(name = "Invitations", description = "Endpoints for managing parche invitations")
public class InvitationRestController {

    private final SendInvitationUseCase sendInvitationUseCase;
    private final AcceptInvitationUseCase acceptInvitationUseCase;

    @PostMapping
    @Operation(summary = "Send invitation", description = "Allows a parche member to invite a student. Common members cannot invite if 'allowedMemberInvitation' is false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created and sent the invitation"),
            @ApiResponse(responseCode = "400", description = "Invalid request or validation error"),
            @ApiResponse(responseCode = "403", description = "Operation not allowed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> sendInvitation(@RequestBody SendInvitationCommand command) {
        UUID invitationId = sendInvitationUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("invitationId", invitationId));
    }

    @PostMapping("/{invitationId}/accept")
    @Operation(summary = "Accept invitation", description = "Allows an invited student to accept a pending invitation and join the parche.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully accepted invitation"),
            @ApiResponse(responseCode = "400", description = "Invalid request or state transition error"),
            @ApiResponse(responseCode = "404", description = "Invitation not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> acceptInvitation(
            @Parameter(description = "ID of the invitation to accept", required = true)
            @PathVariable UUID invitationId,
            @RequestBody AcceptRequest request
    ) {
        acceptInvitationUseCase.execute(invitationId, request.studentId());
        return ResponseEntity.ok(java.util.Map.of("message", "Invitación aceptada correctamente."));
    }

    @Schema(description = "Request body to accept an invitation")
    public record AcceptRequest(
            @Schema(description = "UUID of the student accepting the invitation", requiredMode = Schema.RequiredMode.REQUIRED)
            UUID studentId
    ) {}
}

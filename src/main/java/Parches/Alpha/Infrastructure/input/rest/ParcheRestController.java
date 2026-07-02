package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;
import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Aplication.ports.CreateParcheUseCase;
import Parches.Alpha.Aplication.ports.JoinParcheUseCase;
import Parches.Alpha.Aplication.ports.SearchAvailableParchesUseCase;
import Parches.Alpha.Aplication.ports.CreatePostUseCase;
import Parches.Alpha.Aplication.ports.GetParchePostsUseCase;
import Parches.Alpha.Aplication.ports.GetParcheMembersUseCase;
import Parches.Alpha.Aplication.ports.ImageStoragePort;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.Model.Post;
import Parches.Alpha.Domain.Model.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parches")
@RequiredArgsConstructor
@Tag(name = "Parches", description = "Endpoints for managing parches (groups) and members")
public class ParcheRestController {

    private final CreateParcheUseCase createParcheUseCase;
    private final SearchAvailableParchesUseCase searchAvailableParchesUseCase;
    private final JoinParcheUseCase joinParcheUseCase;
    private final CreatePostUseCase createPostUseCase;
    private final GetParchePostsUseCase getParchePostsUseCase;
    private final GetParcheMembersUseCase getParcheMembersUseCase;
    private final ImageStoragePort imageStoragePort;

    @PostMapping
    @Operation(summary = "Create a new parche", description = "Initialize a new university parche with maximum capacity between 2 and 30, and assign the creator role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parche created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid command fields or quota capacity out of bounds"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createParche(@RequestBody CreateParcheCommand command) {
        UUID parcheId = createParcheUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("parcheId", parcheId));
    }

    @GetMapping
    @Operation(summary = "Search parches", description = "Retrieve active parches matching filter options (category, place, date, availableSlots, search query) with pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved parches list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> searchParches(
            @Parameter(description = "Category to filter by") @RequestParam(required = false) String category,
            @Parameter(description = "Place to filter by") @RequestParam(required = false) String place,
            @Parameter(description = "Date to filter by") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Minimum available slots required") @RequestParam(required = false) Integer availableSlots,
            @Parameter(description = "Free text search query for name/description") @RequestParam(required = false) String query,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size
    ) {
        ParcheQueryFilter filter = new ParcheQueryFilter(category, place, date, availableSlots, query);
        List<Parche> parches = searchAvailableParchesUseCase.execute(filter, page, size);
        return ResponseEntity.ok(parches);
    }

    @PostMapping("/{parcheId}/join")
    @Operation(summary = "Join a parche", description = "Allows a student to join an active parche if the capacity is not exceeded.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully joined parche"),
            @ApiResponse(responseCode = "400", description = "Validation or constraint error"),
            @ApiResponse(responseCode = "404", description = "Parche not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> joinParche(
            @Parameter(description = "ID of the parche to join", required = true) @PathVariable UUID parcheId,
            @RequestBody JoinRequest request
    ) {
        joinParcheUseCase.execute(parcheId, request.studentId());
        return ResponseEntity.ok(java.util.Map.of("message", "Te has unido exitosamente al parche."));
    }

    @PostMapping(value = "/{parcheId}/posts")
    @Operation(summary = "Create a post in a parche", description = "Create a publication (post) inside a parche. Supports text, image URL, and file uploading via multipart form.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created post"),
            @ApiResponse(responseCode = "400", description = "Invalid post text/photo content or member validation error"),
            @ApiResponse(responseCode = "404", description = "Parche not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createPost(
            @Parameter(description = "ID of the parche to post in", required = true) @PathVariable UUID parcheId,
            @Parameter(description = "UUID of the post author") @RequestParam("authorId") UUID authorId,
            @Parameter(description = "Text content of the post") @RequestParam(value = "text", required = false) String text,
            @Parameter(description = "Image URL if already hosted elsewhere") @RequestParam(value = "photoUrl", required = false) String photoUrl,
            @Parameter(description = "Image file attachment to upload") @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        String finalPhotoUrl = photoUrl;
        if (file != null && !file.isEmpty()) {
            finalPhotoUrl = imageStoragePort.store(file);
        }
        createPostUseCase.execute(parcheId, authorId, text, finalPhotoUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("message", "Publicación creada exitosamente."));
    }

    @GetMapping("/{parcheId}/posts")
    @Operation(summary = "Get all posts in a parche", description = "Fetch the feed of publications of a parche, containing comments and reactions for each post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts"),
            @ApiResponse(responseCode = "404", description = "Parche not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getPosts(
            @Parameter(description = "ID of the parche to get posts from", required = true) @PathVariable UUID parcheId
    ) {
        List<Post> posts = getParchePostsUseCase.execute(parcheId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{parcheId}/members")
    @Operation(summary = "Get all members of a parche", description = "Retrieve the list of active members inside a specific parche.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved members"),
            @ApiResponse(responseCode = "404", description = "Parche not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getMembers(
            @Parameter(description = "ID of the parche to retrieve members for", required = true) @PathVariable UUID parcheId
    ) {
        List<Member> members = getParcheMembersUseCase.execute(parcheId);
        return ResponseEntity.ok(members);
    }

    @Schema(description = "Request body to join a parche")
    public record JoinRequest(
            @Schema(description = "UUID of the student wishing to join", requiredMode = Schema.RequiredMode.REQUIRED)
            UUID studentId
    ) {}
}

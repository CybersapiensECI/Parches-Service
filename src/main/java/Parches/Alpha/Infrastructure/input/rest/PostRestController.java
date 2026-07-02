package Parches.Alpha.Infrastructure.input.rest;

import Parches.Alpha.Aplication.ports.CreateCommentUseCase;
import Parches.Alpha.Aplication.ports.FindAllPostsUseCase;
import Parches.Alpha.Aplication.ports.ToggleCommentReactionUseCase;
import Parches.Alpha.Aplication.ports.TogglePostReactionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Posts & Comments", description = "Endpoints for managing posts, comments, and reactions")
public class PostRestController {

    private final CreateCommentUseCase createCommentUseCase;
    private final TogglePostReactionUseCase togglePostReactionUseCase;
    private final ToggleCommentReactionUseCase toggleCommentReactionUseCase;
    private final FindAllPostsUseCase findAllPostsUseCase;

    @GetMapping("/posts")
    @Operation(summary = "Get all posts", description = "Retrieve a global feed of all posts sorted by creation date descending, including comments and reactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved global post feed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(findAllPostsUseCase.execute());
    }

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Add comment to a post", description = "Create a comment on an existing post by providing author ID and text.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created comment"),
            @ApiResponse(responseCode = "400", description = "Invalid request or validation error"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createComment(
            @Parameter(description = "ID of the post to comment on", required = true)
            @PathVariable UUID postId,
            @RequestBody CreateCommentRequest request
    ) {
        createCommentUseCase.execute(postId, request.authorId(), request.text());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Comentario creado exitosamente."));
    }

    @PostMapping("/posts/{postId}/reactions")
    @Operation(summary = "React to a post", description = "Toggle reaction (like) of a student on a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully toggled reaction"),
            @ApiResponse(responseCode = "400", description = "Invalid request or validation error"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> reactToPost(
            @Parameter(description = "ID of the post to react to", required = true)
            @PathVariable UUID postId,
            @RequestBody ReactionRequest request
    ) {
        togglePostReactionUseCase.execute(postId, request.studentId());
        return ResponseEntity.ok(Map.of("message", "Reacción de publicación procesada."));
    }

    @PostMapping("/comments/{commentId}/reactions")
    @Operation(summary = "React to a comment", description = "Toggle reaction (like) of a student on a specific comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully toggled reaction"),
            @ApiResponse(responseCode = "400", description = "Invalid request or validation error"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> reactToComment(
            @Parameter(description = "ID of the comment to react to", required = true)
            @PathVariable UUID commentId,
            @RequestBody ReactionRequest request
    ) {
        toggleCommentReactionUseCase.execute(commentId, request.studentId());
        return ResponseEntity.ok(Map.of("message", "Reacción de comentario procesada."));
    }

    @Schema(description = "Request body to create a comment")
    public record CreateCommentRequest(
            @Schema(description = "UUID of the comment author", requiredMode = Schema.RequiredMode.REQUIRED)
            UUID authorId,
            @Schema(description = "Text content of the comment", requiredMode = Schema.RequiredMode.REQUIRED)
            String text
    ) {}

    @Schema(description = "Request body to react to a post or comment")
    public record ReactionRequest(
            @Schema(description = "UUID of the student reacting", requiredMode = Schema.RequiredMode.REQUIRED)
            UUID studentId
    ) {}
}

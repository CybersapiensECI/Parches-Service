package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.Enums.ParcheCategory;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Enums.ParcheType;
import Parches.Alpha.Domain.Enums.Places;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor // Constructor vacío esencial para frameworks y patrones de diseño
@AllArgsConstructor // Constructor con todo requerido por @Builder
public class Parche {
    private UUID id;
    private String name;
    private String description;
    private Places place;
    private ParcheCategory category;
    private ParcheType type;
    private LocalDate date;
    private LocalTime hour;
    private int maxiumQuota; // Mantén tu typo tal cual lo tienes programado en tus componentes
    private ParcheStatus status;
    private UUID creatorStudentId;
    private LocalDateTime creationDate;
    private boolean allowedMemberInvitation;
    private UUID eventId;

    // Inicializamos las listas por defecto para evitar NullPointerException molestos
    @Builder.Default
    private List<Member> members = new ArrayList<>();

    @Builder.Default
    private List<Invitation> invitations = new ArrayList<>();

    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    /**
     * Valida si el estudiante está activo en un parche por medio de su id.
     */
    public boolean isStudentMember(UUID id){
        if (id == null || this.members == null) return false;
        return this.members.stream()
                .anyMatch(m -> m.getStudentId() != null && m.getStudentId().equals(id));
    }

    /**
     * Permite que el creador del parche cambie los permisos cuando lo desee. Únicamente el creador los puede cambiar.
     */
    public void configureInvitationpermision(UUID requesterId, boolean allow){
        Optional.ofNullable(requesterId)
                .filter(idReq -> idReq.equals(this.creatorStudentId))
                .ifPresentOrElse(
                        idReq -> this.allowedMemberInvitation = allow,
                        () -> {
                            throw new RuntimeException("Solo el creador puede cambiar los permisos de invitaciones");
                        }
                );
    }

    /**
     * Método encargado de la creación de una publicación validando la membresía del autor.
     */
    public void createPost(UUID authorId, String text, String photoUrl) {
        if (authorId == null || !isStudentMember(authorId)) {
            throw new RuntimeException("Solo los miembros activos pueden publicar en el parche.");
        }

        if (this.posts == null) {
            this.posts = new ArrayList<>();
        }

        // Asegúrate de que en Post.java tengas @Builder implementado
        this.posts.add(Post.builder()
                .text(text)
                .photoUrl(photoUrl)
                .build());
    }
}
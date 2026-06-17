package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.Enums.ParcheCategory;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import Parches.Alpha.Domain.Enums.ParcheType;
import Parches.Alpha.Domain.Enums.Places;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
public class Parche {
    private final UUID id;
    private String name;
    private String description;
    private Places place;
    private ParcheCategory category;
    private ParcheType type;
    private LocalDate date;
    private LocalTime hour;
    private int maxiumQuota;
    private ParcheStatus status;
    private final UUID creatorStudentId;
    private final LocalDateTime creationDate;
    private boolean allowedMemberInvitation;
    private UUID eventId;
    private final List<Member> members;
    private final List<Invitation> invitations;
    private final List<Post> posts;

    /*
    * valida si es estudiante esta activo en un parche por medio de su id
     */
    public boolean isStudentMember(UUID id){
        return Optional.ofNullable(id).map(studentid ->this.members.stream().anyMatch(m -> m.getStudentId().equals(studentid)))
                .orElse(false);
    }
    /*
     * permite que que el creador del parche cambie los permiso cuando lo desee, unicamente el creador los puede cambiar
     */
    public void configureInvitationpermision(UUID requesterId, boolean allow){
        Optional.ofNullable(requesterId).filter(this.creatorStudentId::equals)
                .ifPresentOrElse(
                        id ->this.allowedMemberInvitation = allow,
                        () -> {
                            throw new RuntimeException("Solo el crreador puede cambiar los permisos de invitaciones")
                        }

                );
    }
    /**
     * Metodo encargado de la creación de una publicación validando la membresía del autor.
     */
    public void createPost(UUID authorId, String text, String photoUrl) {
        Optional.ofNullable(authorId)
                .filter(this::isStudentMember)
                .orElseThrow(() -> new RuntimeException("Solo los miembros activos pueden publicar en el parche."));

        this.posts.add(Post.builder().text(text).photoUrl(photoUrl).build());
    }


}


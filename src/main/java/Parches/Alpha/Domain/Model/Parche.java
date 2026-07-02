package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.Enums.MemberRole;
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
    private int maximumQuota; // Mantén tu typo tal cual lo tienes programado en tus componentes
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
     * Agrega un nuevo miembro validando capacidad y existencia previa.
     */
    public void addMember(UUID studentId, MemberRole role) {
        if (studentId == null) {
            throw new RuntimeException("El id del estudiante no puede ser nulo.");
        }
        if (isStudentMember(studentId)) {
            throw new RuntimeException("El estudiante ya es miembro de este parche.");
        }
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        if (this.members.size() >= this.maximumQuota) {
            throw new RuntimeException("El parche ha alcanzado su capacidad máxima.");
        }
        Member newMember = Member.builder()
                .parcheId(this.id)
                .studentId(studentId)
                .role(role)
                .build();
        newMember.validateAndInitialize();
        this.members.add(newMember);
    }

    /**
     * Remueve un miembro del parche, prohibiendo que el creador lo abandone sin transferir.
     */
    public void removeMember(UUID studentId) {
        if (studentId == null) {
            throw new RuntimeException("El id del estudiante no puede ser nulo.");
        }
        if (studentId.equals(this.creatorStudentId)) {
            throw new RuntimeException("El creador/capitán no puede salir del parche sin transferir su rol o cerrarlo.");
        }
        if (this.members != null) {
            this.members.removeIf(m -> studentId.equals(m.getStudentId()));
        }
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

        Post newPost = Post.builder()
                .authorId(authorId)
                .text(text)
                .photoUrl(photoUrl)
                .build();
        newPost.validate();
        this.posts.add(newPost);
    }

    /**
     * Agrega un comentario a una publicación específica.
     */
    public void addCommentToPost(UUID authorId, UUID postId, String text) {
        if (authorId == null || !isStudentMember(authorId)) {
            throw new RuntimeException("Solo los miembros activos pueden comentar en el parche.");
        }
        if (this.posts == null) {
            throw new RuntimeException("El parche no contiene publicaciones.");
        }
        this.posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada."))
                .addComment(authorId, text);
    }

    /**
     * Alterna la reacción de un estudiante a una publicación.
     */
    public void togglePostReaction(UUID studentId, UUID postId) {
        if (studentId == null || !isStudentMember(studentId)) {
            throw new RuntimeException("Solo los miembros activos pueden reaccionar a las publicaciones.");
        }
        if (this.posts == null) return;
        this.posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada."))
                .toggleReaction(studentId);
    }

    /**
     * Alterna la reacción de un estudiante a un comentario específico.
     */
    public void toggleCommentReaction(UUID studentId, UUID postId, UUID commentId) {
        if (studentId == null || !isStudentMember(studentId)) {
            throw new RuntimeException("Solo los miembros activos pueden reaccionar a los comentarios.");
        }
        if (this.posts == null) return;
        this.posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada."))
                .toggleCommentReaction(studentId, commentId);
    }

    /**
     * Agrega una invitación validando que no se supere la cuota ni existan duplicados.
     */
    public void inviteStudent(Invitation invitation) {
        if (this.status != ParcheStatus.ACTIVE) {
            throw new RuntimeException("No se pueden enviar invitaciones a un parche que no está activo.");
        }
        if (this.members != null && this.members.size() >= this.maximumQuota) {
            throw new RuntimeException("El parche ha alcanzado su capacidad máxima.");
        }
        if (isStudentMember(invitation.getInvitedStudentId())) {
            throw new RuntimeException("El estudiante ya es miembro de este parche.");
        }
        if (this.invitations == null) {
            this.invitations = new ArrayList<>();
        }
        this.invitations.add(invitation);
    }
}
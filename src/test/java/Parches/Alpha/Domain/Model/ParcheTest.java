package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.Enums.MemberRole;
import Parches.Alpha.Domain.Enums.ParcheStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParcheTest {

    private Parche parche;
    private UUID creatorId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        creatorId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        parche = Parche.builder()
                .id(UUID.randomUUID())
                .name("Parche Futbol")
                .status(ParcheStatus.ACTIVE)
                .maximumQuota(2)
                .creatorStudentId(creatorId)
                .members(new ArrayList<>())
                .invitations(new ArrayList<>())
                .posts(new ArrayList<>())
                .build();
    }

    @Test
    void testIsStudentMember() {
        assertFalse(parche.isStudentMember(null));
        assertFalse(parche.isStudentMember(studentId));

        parche.addMember(studentId, MemberRole.STUDENT);
        assertTrue(parche.isStudentMember(studentId));
    }

    @Test
    void testConfigureInvitationPermission() {
        parche.configureInvitationpermision(creatorId, false);
        assertFalse(parche.isAllowedMemberInvitation());

        assertThrows(RuntimeException.class, () ->
                parche.configureInvitationpermision(studentId, true)
        );
    }

    @Test
    void testAddMemberSuccessAndLimit() {
        parche.addMember(creatorId, MemberRole.CREATOR);
        parche.addMember(studentId, MemberRole.STUDENT);

        assertEquals(2, parche.getMembers().size());

        UUID extraId = UUID.randomUUID();
        assertThrows(RuntimeException.class, () ->
                parche.addMember(extraId, MemberRole.STUDENT)
        );
    }

    @Test
    void testAddMemberNullOrDuplicate() {
        assertThrows(RuntimeException.class, () -> parche.addMember(null, MemberRole.STUDENT));

        parche.addMember(studentId, MemberRole.STUDENT);
        assertThrows(RuntimeException.class, () -> parche.addMember(studentId, MemberRole.STUDENT));
    }

    @Test
    void testRemoveMember() {
        assertThrows(RuntimeException.class, () -> parche.removeMember(null));
        assertThrows(RuntimeException.class, () -> parche.removeMember(creatorId));

        parche.addMember(studentId, MemberRole.STUDENT);
        parche.removeMember(studentId);
        assertFalse(parche.isStudentMember(studentId));
    }

    @Test
    void testCreatePostAndCommentsAndReactions() {
        assertThrows(RuntimeException.class, () ->
                parche.createPost(studentId, "Texto", null)
        );

        parche.addMember(studentId, MemberRole.STUDENT);
        parche.createPost(studentId, "Texto", "http://image.jpg");
        assertEquals(1, parche.getPosts().size());

        Post post = parche.getPosts().get(0);
        post.setId(UUID.randomUUID());

        assertThrows(RuntimeException.class, () ->
                parche.addCommentToPost(creatorId, post.getId(), "Comentario")
        );
        parche.addCommentToPost(studentId, post.getId(), "Comentario");
        assertEquals(1, post.getComments().size());

        parche.togglePostReaction(studentId, post.getId());
        assertEquals(1, post.getReactions().size());
        parche.togglePostReaction(studentId, post.getId());
        assertEquals(0, post.getReactions().size());

        Comment comment = post.getComments().get(0);
        comment.setId(UUID.randomUUID());
        parche.toggleCommentReaction(studentId, post.getId(), comment.getId());
        assertEquals(1, comment.getReactions().size());
    }

    @Test
    void testInviteStudent() {
        Invitation invitation = Invitation.builder()
                .parcheId(parche.getId())
                .inviterId(creatorId)
                .invitedStudentId(studentId)
                .build();

        parche.inviteStudent(invitation);
        assertEquals(1, parche.getInvitations().size());

        parche.setStatus(ParcheStatus.FILED);
        assertThrows(RuntimeException.class, () -> parche.inviteStudent(invitation));
    }
}

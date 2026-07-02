package Parches.Alpha.Domain.Model;

import Parches.Alpha.Domain.Enums.MemberRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelsTest {

    @Test
    void testMemberModel() {
        UUID studentId = UUID.randomUUID();
        UUID parcheId = UUID.randomUUID();

        Member member = Member.builder()
                .studentId(studentId)
                .parcheId(parcheId)
                .build();

        member.validateAndInitialize();

        assertNotNull(member.getId());
        assertEquals(MemberRole.STUDENT, member.getRole());
        assertNotNull(member.getUnionDate());

        Member memberEmpty = new Member();
        assertThrows(RuntimeException.class, memberEmpty::validateAndInitialize);

        memberEmpty.setStudentId(studentId);
        assertThrows(RuntimeException.class, memberEmpty::validateAndInitialize);
    }

    @Test
    void testPostModel() {
        Post post = new Post();
        post.setText("  ");
        post.setPhotoUrl("  ");
        assertThrows(RuntimeException.class, post::validate);

        post.setText("Hola");
        post.validate();
        assertNotNull(post.getUnmodifiableComments());
        assertNotNull(post.getUnmodifiableReactions());

        post.setComments(null);
        post.setReactions(null);
        assertTrue(post.getUnmodifiableComments().isEmpty());
        assertTrue(post.getUnmodifiableReactions().isEmpty());
    }

    @Test
    void testCommentModel() {
        Comment comment = new Comment();
        assertThrows(RuntimeException.class, comment::validate);

        comment.setAuthorId(UUID.randomUUID());
        assertThrows(RuntimeException.class, comment::validate);

        comment.setText("Comentario");
        comment.validate();

        assertThrows(RuntimeException.class, () -> comment.toggleReaction(null));
    }

    @Test
    void testReactionModel() {
        Reaction reaction = new Reaction();
        assertThrows(NullPointerException.class, reaction::validate);

        reaction.setStudentId(UUID.randomUUID());
        reaction.validate();
    }
}

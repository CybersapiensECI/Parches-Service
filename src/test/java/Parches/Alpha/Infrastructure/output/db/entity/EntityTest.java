package Parches.Alpha.Infrastructure.output.db.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testParcheEntity() {
        UUID id = UUID.randomUUID();
        ParcheEntity entity = new ParcheEntity();
        entity.setId(id);
        entity.setName("Futbol");
        entity.setDescription("Partido");
        entity.setLugar("SOCCER_COURT_1");
        entity.setCategory("SPORTS");
        entity.setType("PUBLIC");
        entity.setStatus("ACTIVE");
        entity.setDate(LocalDate.now());
        entity.setHour(LocalTime.now());
        entity.setMaximumQuota(10);
        entity.setCreatorStudentId(UUID.randomUUID());
        entity.setEventId(UUID.randomUUID());
        entity.setImageUrl("http://image.jpg");
        entity.setMembers(new ArrayList<>());
        entity.setPosts(new ArrayList<>());

        entity.prePersist();
        assertNotNull(entity.getCreationDate());
        assertTrue(entity.isNew());
        entity.markNotNew();
        assertFalse(entity.isNew());

        assertEquals(id, entity.getId());
        assertEquals("Futbol", entity.getName());
        assertEquals("Partido", entity.getDescription());
        assertEquals("SOCCER_COURT_1", entity.getLugar());
        assertEquals("SPORTS", entity.getCategory());
        assertEquals("PUBLIC", entity.getType());
        assertEquals("ACTIVE", entity.getStatus());
        assertEquals(10, entity.getMaximumQuota());
        assertNotNull(entity.getMembers());
        assertNotNull(entity.getPosts());
        assertEquals("http://image.jpg", entity.getImageUrl());
    }

    @Test
    void testInvitationEntity() {
        UUID id = UUID.randomUUID();
        InvitationEntity entity = new InvitationEntity();
        entity.setId(id);
        entity.setParcheId(UUID.randomUUID());
        entity.setSenderId(UUID.randomUUID());
        entity.setInvitedId(UUID.randomUUID());
        entity.setState("PENDING");
        entity.setRespondedAt(LocalDateTime.now());

        entity.prePersist();
        assertNotNull(entity.getSendAt());
        assertTrue(entity.isNew());
        entity.markNotNew();
        assertFalse(entity.isNew());

        assertEquals(id, entity.getId());
        assertEquals("PENDING", entity.getState());
        assertNotNull(entity.getRespondedAt());
    }

    @Test
    void testMemberEntity() {
        UUID id = UUID.randomUUID();
        MemberEntity entity = new MemberEntity();
        entity.setId(id);
        entity.setStudentId(UUID.randomUUID());
        entity.setUnionDate(LocalDateTime.now());
        entity.setRole("STUDENT");
        entity.setParche(new ParcheEntity());

        assertTrue(entity.isNew());
        entity.markNotNew();
        assertFalse(entity.isNew());

        assertEquals(id, entity.getId());
        assertEquals("STUDENT", entity.getRole());
        assertNotNull(entity.getParche());
    }

    @Test
    void testPostEntity() {
        UUID id = UUID.randomUUID();
        PostEntity entity = new PostEntity();
        entity.setId(id);
        entity.setText("Hola");
        entity.setPhotoUrl("http://image.jpg");
        entity.setAuthorId(UUID.randomUUID());
        entity.setParche(new ParcheEntity());
        entity.setComments(new ArrayList<>());
        entity.setReactions(new ArrayList<>());

        entity.setCreatedAt(LocalDateTime.now());
        assertTrue(entity.isNew());
        entity.markNotNew();
        assertFalse(entity.isNew());

        assertEquals(id, entity.getId());
        assertEquals("Hola", entity.getText());
        assertEquals("http://image.jpg", entity.getPhotoUrl());
        assertNotNull(entity.getParche());
        assertNotNull(entity.getComments());
        assertNotNull(entity.getReactions());
    }

    @Test
    void testCommentEntity() {
        UUID id = UUID.randomUUID();
        CommentEntity entity = new CommentEntity();
        entity.setId(id);
        entity.setAuthorId(UUID.randomUUID());
        entity.setText("Texto");
        entity.setPost(new PostEntity());
        entity.setReactions(new ArrayList<>());

        entity.setCreatedAt(LocalDateTime.now());
        assertTrue(entity.isNew());
        entity.markNotNew();
        assertFalse(entity.isNew());

        assertEquals(id, entity.getId());
        assertEquals("Texto", entity.getText());
        assertNotNull(entity.getPost());
        assertNotNull(entity.getReactions());
    }

    @Test
    void testReactionEntity() {
        UUID id = UUID.randomUUID();
        ReactionEntity entity = new ReactionEntity();
        entity.setId(id);
        entity.setStudentId(UUID.randomUUID());
        entity.setPost(new PostEntity());
        entity.setComment(new CommentEntity());

        entity.setCreatedAt(LocalDateTime.now());
        assertTrue(entity.isNew());
        entity.markNotNew();
        assertFalse(entity.isNew());

        assertEquals(id, entity.getId());
        assertNotNull(entity.getPost());
        assertNotNull(entity.getComment());
    }
}

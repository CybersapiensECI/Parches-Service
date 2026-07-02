package Parches.Alpha.Infrastructure.output.db.mapper;

import Parches.Alpha.Domain.Enums.*;
import Parches.Alpha.Domain.Model.*;
import Parches.Alpha.Infrastructure.output.db.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParcheMapperTest {

    @Test
    void testToDomainNull() {
        assertNull(ParcheMapper.toDomain(null));
    }

    @Test
    void testToEntityNull() {
        assertNull(ParcheMapper.toEntity(null));
    }

    @Test
    void testToDomainAndEntityComplete() {
        UUID parcheId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID creatorId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        UUID commentId = UUID.randomUUID();
        UUID reactionId = UUID.randomUUID();

        Reaction reaction = Reaction.builder()
                .id(reactionId)
                .studentId(studentId)
                .createdAt(LocalDateTime.now())
                .build();

        Comment comment = Comment.builder()
                .id(commentId)
                .authorId(studentId)
                .text("Que bien!")
                .createdAt(LocalDateTime.now())
                .reactions(new ArrayList<>(Collections.singletonList(reaction)))
                .build();

        Post post = Post.builder()
                .id(postId)
                .text("Hola")
                .photoUrl("http://image.jpg")
                .createdAt(LocalDateTime.now())
                .authorId(studentId)
                .comments(new ArrayList<>(Collections.singletonList(comment)))
                .reactions(new ArrayList<>(Collections.singletonList(reaction)))
                .build();

        Member member = Member.builder()
                .id(UUID.randomUUID())
                .parcheId(parcheId)
                .studentId(studentId)
                .role(MemberRole.STUDENT)
                .unionDate(LocalDateTime.now())
                .build();

        Parche parche = Parche.builder()
                .id(parcheId)
                .name("Parche de Cine")
                .description("Cine foro")
                .place(Places.BUILDING_C)
                .category(ParcheCategory.CINEMA)
                .type(ParcheType.PUBLIC)
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .maximumQuota(15)
                .status(ParcheStatus.ACTIVE)
                .creatorStudentId(creatorId)
                .creationDate(LocalDateTime.now())
                .eventId(eventId)
                .members(new ArrayList<>(Collections.singletonList(member)))
                .posts(new ArrayList<>(Collections.singletonList(post)))
                .build();

        ParcheEntity entity = ParcheMapper.toEntity(parche);

        assertNotNull(entity);
        assertEquals(parcheId, entity.getId());
        assertEquals("Parche de Cine", entity.getName());
        assertEquals("BUILDING_C", entity.getLugar());
        assertEquals("CINEMA", entity.getCategory());
        assertEquals("PUBLIC", entity.getType());
        assertEquals("ACTIVE", entity.getStatus());
        assertEquals(1, entity.getMembers().size());
        assertEquals(1, entity.getPosts().size());

        PostEntity postEntity = entity.getPosts().get(0);
        assertEquals("Hola", postEntity.getText());
        assertEquals(1, postEntity.getComments().size());
        assertEquals(1, postEntity.getReactions().size());

        CommentEntity commentEntity = postEntity.getComments().get(0);
        assertEquals("Que bien!", commentEntity.getText());
        assertEquals(1, commentEntity.getReactions().size());

        Parche domainMapped = ParcheMapper.toDomain(entity);

        assertNotNull(domainMapped);
        assertEquals(parcheId, domainMapped.getId());
        assertEquals(Places.BUILDING_C, domainMapped.getPlace());
        assertEquals(ParcheCategory.CINEMA, domainMapped.getCategory());
        assertEquals(ParcheType.PUBLIC, domainMapped.getType());
        assertEquals(ParcheStatus.ACTIVE, domainMapped.getStatus());
        assertEquals(1, domainMapped.getMembers().size());
        assertEquals(1, domainMapped.getPosts().size());

        Post postMapped = domainMapped.getPosts().get(0);
        assertEquals("Hola", postMapped.getText());
        assertEquals(1, postMapped.getComments().size());
        assertEquals(1, postMapped.getReactions().size());

        Comment commentMapped = postMapped.getComments().get(0);
        assertEquals("Que bien!", commentMapped.getText());
        assertEquals(1, commentMapped.getReactions().size());
    }

    @Test
    void testToDomainAndEntityNullFields() {
        Parche parche = Parche.builder()
                .id(UUID.randomUUID())
                .members(null)
                .posts(null)
                .build();

        ParcheEntity entity = ParcheMapper.toEntity(parche);
        assertNotNull(entity);
        assertNull(entity.getMembers());
        assertNull(entity.getPosts());

        ParcheEntity parcheEntity = ParcheEntity.builder()
                .id(UUID.randomUUID())
                .members(null)
                .posts(null)
                .build();

        Parche domain = ParcheMapper.toDomain(parcheEntity);
        assertNotNull(domain);
        assertTrue(domain.getMembers().isEmpty());
        assertTrue(domain.getPosts().isEmpty());
    }
}

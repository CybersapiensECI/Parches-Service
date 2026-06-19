package Parches.Alpha.Infrastructure.output.db.mapper;

import Parches.Alpha.Domain.Model.*;
import Parches.Alpha.Domain.Enums.*;
import Parches.Alpha.Infrastructure.output.db.entity.*;
import java.util.stream.Collectors;
import java.util.*;

public class ParcheMapper {

    public static Parche toDomain(ParcheEntity entity) {
        if (entity == null) return null;

        Parche domain = Parche.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .place(entity.getLugar() != null ? Places.valueOf(entity.getLugar()) : null)
                .category(entity.getCategory() != null ? ParcheCategory.valueOf(entity.getCategory()) : null)
                .type(entity.getType() != null ? ParcheType.valueOf(entity.getType()) : null)
                .date(entity.getDate())
                .hour(entity.getHour())
                .maximumQuota(entity.getMaximumQuota())
                .status(entity.getStatus() != null ? ParcheStatus.valueOf(entity.getStatus()) : null)
                .creatorStudentId(entity.getCreatorStudentId())
                .creationDate(entity.getCreationDate())
                .eventId(entity.getEventId())
                .build();

        if (entity.getMembers() != null) {
            domain.setMembers(entity.getMembers().stream()
                    .map(ParcheMapper::toMemberDomain)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        if (entity.getPosts() != null) {
            domain.setPosts(entity.getPosts().stream()
                    .map(ParcheMapper::toPostDomain)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        return domain;
    }

    public static ParcheEntity toEntity(Parche domain) {
        if (domain == null) return null;

        ParcheEntity entity = ParcheEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .lugar(domain.getPlace() != null ? domain.getPlace().name() : null)
                .category(domain.getCategory() != null ? domain.getCategory().name() : null)
                .type(domain.getType() != null ? domain.getType().name() : null)
                .date(domain.getDate())
                .hour(domain.getHour())
                .maximumQuota(domain.getMaximumQuota())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .creatorStudentId(domain.getCreatorStudentId())
                .creationDate(domain.getCreationDate())
                .eventId(domain.getEventId())
                .build();

        if (domain.getMembers() != null) {
            entity.setMembers(domain.getMembers().stream()
                    .map(m -> toMemberEntity(m, entity))
                    .collect(Collectors.toList()));
        }

        if (domain.getPosts() != null) {
            entity.setPosts(domain.getPosts().stream()
                    .map(p -> toPostEntity(p, entity))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private static Member toMemberDomain(MemberEntity entity) {
        if (entity == null) return null;
        return Member.builder()
                .id(entity.getId())
                .parcheId(entity.getParche() != null ? entity.getParche().getId() : null)
                .studentId(entity.getStudentId())
                .unionDate(entity.getUnionDate())
                .role(entity.getRole() != null ? MemberRole.valueOf(entity.getRole()) : null)
                .build();
    }

    private static MemberEntity toMemberEntity(Member domain, ParcheEntity parcheEntity) {
        if (domain == null) return null;
        return MemberEntity.builder()
                .id(domain.getId())
                .studentId(domain.getStudentId())
                .unionDate(domain.getUnionDate())
                .role(domain.getRole() != null ? domain.getRole().name() : null)
                .parche(parcheEntity)
                .build();
    }

    private static Post toPostDomain(PostEntity entity) {
        if (entity == null) return null;
        Post domain = Post.builder()
                .id(entity.getId())
                .text(entity.getText())
                .photoUrl(entity.getPhotoUrl())
                .createdAt(entity.getCreatedAt())
                .authorId(entity.getAuthorId())
                .build();

        if (entity.getComments() != null) {
            domain.setComments(entity.getComments().stream()
                    .map(ParcheMapper::toCommentDomain)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        if (entity.getReactions() != null) {
            domain.setReactions(entity.getReactions().stream()
                    .map(ParcheMapper::toReactionDomain)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        return domain;
    }

    private static PostEntity toPostEntity(Post domain, ParcheEntity parcheEntity) {
        if (domain == null) return null;
        PostEntity entity = PostEntity.builder()
                .id(domain.getId())
                .text(domain.getText())
                .photoUrl(domain.getPhotoUrl())
                .createdAt(domain.getCreatedAt())
                .authorId(domain.getAuthorId())
                .parche(parcheEntity)
                .build();

        if (domain.getComments() != null) {
            entity.setComments(domain.getComments().stream()
                    .map(c -> toCommentEntity(c, entity))
                    .collect(Collectors.toList()));
        }

        if (domain.getReactions() != null) {
            entity.setReactions(domain.getReactions().stream()
                    .map(r -> toReactionEntity(r, entity, null))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private static Comment toCommentDomain(CommentEntity entity) {
        if (entity == null) return null;
        Comment domain = Comment.builder()
                .id(entity.getId())
                .authorId(entity.getAuthorId())
                .text(entity.getText())
                .createdAt(entity.getCreatedAt())
                .build();

        if (entity.getReactions() != null) {
            domain.setReactions(entity.getReactions().stream()
                    .map(ParcheMapper::toReactionDomain)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        return domain;
    }

    private static CommentEntity toCommentEntity(Comment domain, PostEntity postEntity) {
        if (domain == null) return null;
        CommentEntity entity = CommentEntity.builder()
                .id(domain.getId())
                .authorId(domain.getAuthorId())
                .text(domain.getText())
                .createdAt(domain.getCreatedAt())
                .post(postEntity)
                .build();

        if (domain.getReactions() != null) {
            entity.setReactions(domain.getReactions().stream()
                    .map(r -> toReactionEntity(r, null, entity))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private static Reaction toReactionDomain(ReactionEntity entity) {
        if (entity == null) return null;
        return Reaction.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private static ReactionEntity toReactionEntity(Reaction domain, PostEntity postEntity, CommentEntity commentEntity) {
        if (domain == null) return null;
        return ReactionEntity.builder()
                .id(domain.getId())
                .studentId(domain.getStudentId())
                .createdAt(domain.getCreatedAt())
                .post(postEntity)
                .comment(commentEntity)
                .build();
    }
}

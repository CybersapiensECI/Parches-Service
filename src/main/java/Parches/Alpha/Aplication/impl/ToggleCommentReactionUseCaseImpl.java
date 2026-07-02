package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.ToggleCommentReactionUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.Model.Post;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ToggleCommentReactionUseCaseImpl implements ToggleCommentReactionUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public ToggleCommentReactionUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public void execute(UUID commentId, UUID studentId) {
        Parche parche = parcheRepository.findByCommentId(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado para reaccionar."));
        
        UUID postId = parche.getPosts().stream()
                .filter(post -> post.getComments().stream().anyMatch(comment -> comment.getId().equals(commentId)))
                .map(Post::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Publicación asociada al comentario no encontrada."));

        parche.toggleCommentReaction(studentId, postId, commentId);
        parcheRepository.save(parche);
    }
}

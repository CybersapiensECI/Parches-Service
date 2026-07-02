package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.CreateCommentUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CreateCommentUseCaseImpl implements CreateCommentUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public CreateCommentUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public void execute(UUID postId, UUID authorId, String text) {
        Parche parche = parcheRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada para agregar comentario."));
        parche.addCommentToPost(authorId, postId, text);
        parcheRepository.save(parche);
    }
}

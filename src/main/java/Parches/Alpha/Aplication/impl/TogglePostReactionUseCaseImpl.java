package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.TogglePostReactionUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class TogglePostReactionUseCaseImpl implements TogglePostReactionUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public TogglePostReactionUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public void execute(UUID postId, UUID studentId) {
        Parche parche = parcheRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada para reaccionar."));
        parche.togglePostReaction(studentId, postId);
        parcheRepository.save(parche);
    }
}

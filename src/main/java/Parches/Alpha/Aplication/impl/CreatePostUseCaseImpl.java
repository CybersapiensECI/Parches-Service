package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.CreatePostUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CreatePostUseCaseImpl implements CreatePostUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public CreatePostUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public void execute(UUID parcheId, UUID authorId, String text, String photoUrl) {
        Parche parche = parcheRepository.findById(parcheId)
                .orElseThrow(() -> new RuntimeException("Parche no encontrado con id: " + parcheId));
        parche.createPost(authorId, text, photoUrl);
        parcheRepository.save(parche);
    }
}

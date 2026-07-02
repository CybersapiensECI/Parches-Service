package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.GetParchePostsUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.Model.Post;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetParchePostsUseCaseImpl implements GetParchePostsUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public GetParchePostsUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public List<Post> execute(UUID parcheId) {
        Parche parche = parcheRepository.findById(parcheId)
                .orElseThrow(() -> new RuntimeException("Parche no encontrado con id: " + parcheId));
        return parche.getPosts();
    }
}

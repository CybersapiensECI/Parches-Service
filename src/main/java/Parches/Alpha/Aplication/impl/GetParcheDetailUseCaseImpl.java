package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.GetParcheDetailUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetParcheDetailUseCaseImpl implements GetParcheDetailUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public GetParcheDetailUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public Parche execute(UUID parcheId) {
        return parcheRepository.findById(parcheId)
                .orElseThrow(() -> new RuntimeException("Parche no encontrado con id: " + parcheId));
    }
}

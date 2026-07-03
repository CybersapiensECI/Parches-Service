package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.ports.GetUserParchesUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetUserParchesUseCaseImpl implements GetUserParchesUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public GetUserParchesUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public List<Parche> execute(UUID userId) {
        return parcheRepository.findByStudentId(userId);
    }
}

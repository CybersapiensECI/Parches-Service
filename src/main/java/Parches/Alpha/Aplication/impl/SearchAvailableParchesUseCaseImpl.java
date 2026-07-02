package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Aplication.ports.SearchAvailableParchesUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchAvailableParchesUseCaseImpl implements SearchAvailableParchesUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public SearchAvailableParchesUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public List<Parche> execute(ParcheQueryFilter filter, int page, int size) {
        return parcheRepository.findAllWithFilters(filter, page, size);
    }
}

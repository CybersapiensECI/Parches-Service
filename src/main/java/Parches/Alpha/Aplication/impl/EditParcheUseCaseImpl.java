package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.dto.UpdateParcheCommand;
import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.exception.ParcheFullException;
import Parches.Alpha.Aplication.ports.EditParcheUseCase;
import Parches.Alpha.Domain.Enums.*;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EditParcheUseCaseImpl implements EditParcheUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public EditParcheUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public void execute(UUID parcheId, UpdateParcheCommand command, UUID requesterId) {
        Parche parche = parcheRepository.findById(parcheId)
                .orElseThrow(() -> new RuntimeException("Parche no encontrado."));

        if (!parche.getCreatorStudentId().equals(requesterId)) {
            throw new ActionNotAllowedException("Solo el capitán del parche puede editar los datos.");
        }

        if (command.maximumQuota() < 2 || command.maximumQuota() > 30) {
            throw new ParcheFullException("Cupo inválido. El cupo debe estar entre 2 y 30.");
        }
        if (parche.getMembers() != null && parche.getMembers().size() > command.maximumQuota()) {
            throw new ParcheFullException("El nuevo cupo máximo no puede ser menor a la cantidad de miembros actuales.");
        }

        parche.setName(command.name());
        parche.setDescription(command.description());
        parche.setPlace(Places.valueOf(command.place().toUpperCase()));
        parche.setCategory(ParcheCategory.valueOf(command.category().toUpperCase()));
        parche.setType(ParcheType.valueOf(command.type().toUpperCase()));
        parche.setDate(command.date());
        parche.setHour(command.hour());
        parche.setMaximumQuota(command.maximumQuota());

        parcheRepository.save(parche);
    }
}

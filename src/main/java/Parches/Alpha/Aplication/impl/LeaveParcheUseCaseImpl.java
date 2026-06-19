package Parches.Alpha.Aplication.impl;

import Parches.Alpha.Aplication.exception.ActionNotAllowedException;
import Parches.Alpha.Aplication.ports.LeaveParcheUseCase;
import Parches.Alpha.Domain.Model.Parche;
import Parches.Alpha.Domain.spi.ParcheRepositorySPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LeaveParcheUseCaseImpl implements LeaveParcheUseCase {

    private final ParcheRepositorySPI parcheRepository;

    @Autowired
    public LeaveParcheUseCaseImpl(ParcheRepositorySPI parcheRepository) {
        this.parcheRepository = parcheRepository;
    }

    @Override
    public void execute(UUID parcheId, UUID studentId) {
        Parche parche = parcheRepository.findById(parcheId)
                .orElseThrow(() -> new RuntimeException("Parche no encontrado."));

        if (!parche.isStudentMember(studentId)) {
            throw new RuntimeException("El estudiante no es miembro de este parche.");
        }

        if (parche.getCreatorStudentId().equals(studentId)) {
            throw new ActionNotAllowedException("El capitán no puede salir del parche sin antes transferir su rol o cerrar el parche.");
        }

        parche.removeMember(studentId);
        parcheRepository.save(parche);
    }
}

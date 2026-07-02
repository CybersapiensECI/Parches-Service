package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Aplication.dto.UpdateParcheCommand;

import java.util.UUID;

public interface EditParcheUseCase {
    void execute(UUID parcheId, UpdateParcheCommand command, UUID requesterId);
}
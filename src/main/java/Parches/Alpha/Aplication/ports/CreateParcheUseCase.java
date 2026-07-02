package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Aplication.dto.CreateParcheCommand;

import java.util.UUID;

public interface CreateParcheUseCase {
    UUID execute(CreateParcheCommand command);
}

package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Domain.Model.Parche;

import java.util.UUID;

public interface GetParcheDetailUseCase {
    Parche execute(UUID parcheId);
}

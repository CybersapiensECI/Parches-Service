package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Domain.Model.Parche;

import java.util.List;
import java.util.UUID;

public interface GetUserParchesUseCase {
    List<Parche> execute(UUID userId);
}

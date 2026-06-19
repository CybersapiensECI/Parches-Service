package Parches.Alpha.Aplication.ports;

import Parches.Alpha.Aplication.dto.ParcheQueryFilter;
import Parches.Alpha.Domain.Model.Parche;

import java.util.List;

public interface SearchAvailableParchesUseCase {
    List<Parche> execute(ParcheQueryFilter filter, int page, int size);
}

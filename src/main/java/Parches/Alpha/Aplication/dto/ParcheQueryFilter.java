package Parches.Alpha.Aplication.dto;

import java.time.LocalDate;

public record ParcheQueryFilter(
        String category,
        String place,
        LocalDate date,
        Integer availableSlots,
        String query
) {}

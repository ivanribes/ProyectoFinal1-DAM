package Pagos;

import java.time.LocalDate;

public interface Penalizable {

    double calcularPenalizacion(LocalDate fechaLimite , LocalDate fechaActual);
}

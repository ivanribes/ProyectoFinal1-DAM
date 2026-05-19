package Pagos;

import Enums.EstadoPago;
import Usuarios.Usuario;

import java.time.LocalDate;

public class Pago {

    private static int ID_PAGO = 0;

    private double importe;
    private LocalDate fechaPago;
    private EstadoPago estadoPago;

    public Pago(double importe) {
        this.importe = importe;
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void confirmar(Usuario usuario) {
        this.fechaPago = LocalDate.now();
    }
}

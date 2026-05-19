package Pagos;

import Enums.EstadoPago;
import Usuarios.Usuario;

import java.time.LocalDate;

public class Pago {

    private static int ID_PAGO = 0;

    private int id;
    private double importe;
    private LocalDate fechaPago;
    private EstadoPago estadoPago;

    public Pago(double importe) {
        this.id = ++ID_PAGO;
        this.importe = importe;
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    public int getId() {
        return id;
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

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public void confirmarPago() {
        this.fechaPago = LocalDate.now();
    }
}

package Pagos;

import Enums.EstadoPago;

import java.time.LocalDate;

public class Pago {

    private static int ID_PAGO = 0;

    private final int id;
    private double importeBase;
    private double penalizacionAplicada;
    private double importeFinal;
    private LocalDate fechaPago;
    private EstadoPago estadoPago;

    public Pago(double importeBase) {
        this.id = ++ID_PAGO;
        this.importeBase = importeBase;
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    public int getId() {
        return id;
    }

    public double getImporteBase() {
        return importeBase;
    }

    public void setImporteBase(double importeBase) {
        this.importeBase = importeBase;
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

    public void setPenalizacionAplicada(double penalizacionAplicada) {
        this.penalizacionAplicada = penalizacionAplicada;
    }

    public void setImporteFinal() {
        this.importeFinal = importeBase+penalizacionAplicada;
    }

    public double calcularPenalizacion() {

        return 0;
    }
}

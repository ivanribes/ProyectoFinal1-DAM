package Pagos;

import Enums.EstadoPago;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Pago implements Penalizable {

    private static final double PENALIZACION = 0.5;

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

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setPenalizacionAplicada(double penalizacionAplicada) {
        this.penalizacionAplicada = penalizacionAplicada;
        setImporteFinal();
    }

    public double getImporteFinal() {
        return importeFinal;
    }

    public double getPenalizacionAplicada() {
        return penalizacionAplicada;
    }

    public void setImporteFinal() {
        this.importeFinal = importeBase + penalizacionAplicada;
    }


    @Override
    public double calcularPenalizacion(LocalDate fechaLimite, LocalDate fechaActual) {
        int diasRetraso = calcularDias(fechaLimite, fechaActual);

        if (diasRetraso > 0) {
            return diasRetraso * PENALIZACION;
        }

        return 0;
    }

    public int calcularDias(LocalDate fechaLimite, LocalDate fechaActual) {
        return (int) ChronoUnit.DAYS.between(fechaLimite, fechaActual);
    }
}

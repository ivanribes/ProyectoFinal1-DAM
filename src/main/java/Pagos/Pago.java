package Pagos;

import Enums.EstadoPago;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Pago implements Penalizable {

    private static final double PENALIZACION = 0.5;

    private double importeBase;
    private double penalizacionAplicada;
    private double importeFinal;
    private LocalDate fechaPago;
    private EstadoPago estadoPago;

    public Pago(double importeBase) {
        this.importeBase = importeBase;
        this.estadoPago = EstadoPago.PENDIENTE;
        setImporteFinal();
    }

    public Pago(double importeBase, double penalizacionAplicada, double importeFinal, LocalDate fechaPago, EstadoPago estadoPago) {
        this.importeBase = importeBase;
        this.penalizacionAplicada = penalizacionAplicada;
        this.importeFinal = importeFinal;
        this.fechaPago = fechaPago;
        this.estadoPago = estadoPago;
    }

    //region GETTERS Y SETTERS


    public double getImporteBase() {
        return importeBase;
    }

    public void setImporteBase(double importeBase) {
        this.importeBase = importeBase;
        setImporteFinal();
    }

    public double getPenalizacionAplicada() {
        return penalizacionAplicada;
    }

    public void setPenalizacionAplicada(double penalizacionAplicada) {
        this.penalizacionAplicada = penalizacionAplicada;
        setImporteFinal();
    }

    public double getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal() {
        this.importeFinal = importeBase + penalizacionAplicada;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }
    //endregion

    //region PENALIZACIÓN
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
    //endregion

    //region ESTADOS DE PAGO
    public boolean estaPendienteConfirmacion() {
        return estadoPago == EstadoPago.PENDIENTE_CONFIRMAR;
    }

    public boolean puedeSerSaldado() {
        return estadoPago == EstadoPago.PENDIENTE ||
                estadoPago == EstadoPago.RECHAZADO;
    }

    public boolean solicitarConfirmacion(LocalDate fechaPago) {
        if (!puedeSerSaldado()) {
            System.out.println("Este pago no puede ser saldado.");
            return false;
        }

        if (fechaPago == null) {
            System.out.println("La fecha de pago no puede ser nula.");
            return false;
        }

        this.fechaPago = fechaPago;
        this.estadoPago = EstadoPago.PENDIENTE_CONFIRMAR;
        return true;
    }

    public void confirmar() {
        if (!estaPendienteConfirmacion()) {
            System.out.println("Solo se pueden confirmar pagos pendientes de confirmación.");
            return;
        }

        if (fechaPago == null) {
            System.out.println("No se puede confirmar un pago sin fecha de pago.");
            return;
        }

        this.estadoPago = EstadoPago.PAGADO;
    }

    public void rechazar() {
        if (!estaPendienteConfirmacion()) {
            System.out.println("Solo se pueden rechazar pagos pendientes de confirmación.");
            return;
        }

        this.estadoPago = EstadoPago.RECHAZADO;
    }
    //endregion
}

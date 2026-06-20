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
        setImporteFinal();
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

    public boolean estaPendienteConfirmacion() {
        return estadoPago == EstadoPago.PENDIENTE_CONFIRMAR;
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
}

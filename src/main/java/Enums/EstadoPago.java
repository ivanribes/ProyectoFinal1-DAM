package Enums;

public enum EstadoPago {

    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    PENDIENTE_CONFIRMAR("Pendiente de confirmación"),
    RECHAZADO("Rechazado");

    private String texto;

    EstadoPago(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    @Override public String toString() {
        return texto;
    }
}

package co.edu.uniquindio.Util;

public class TransaccionConstantes {
    public static final String TIPO_DEPOSITO_CUENTA = "DEPÓSITO A CUENTA";
    public static final String TIPO_DEPOSITO_PRESUPUESTO = "DEPÓSITO A PRESUPUESTO";
    public static final String TIPO_RETIRO_CUENTA = "RETIRO DE CUENTA";
    public static final String TIPO_RETIRO_PRESUPUESTO = "RETIRO DE PRESUPUESTO";
    public static final String TIPO_PRESUPUESTO = "PRESUPUESTO";
    public static final String TIPO_CATEGORIA = "CATEGORIA";
    public static final String TIPO_TRANSFERENCIA = "TRANSFERENCIA";

    public static final String TIPO_AJUSTE_POSITIVO = "AJUSTE POSITIVO [ADMIN]";
    public static final String TIPO_AJUSTE_NEGATIVO = "AJUSTE NEGATIVO [ADMIN]";
    public static final String TIPO_DEPOSITO_INICIAL = "DEPÓSITO INICIAL [ADMIN]";
    public static final String TIPO_BONIFICACION = "BONIFICACIÓN [ADMIN]";
    public static final String TIPO_PENALIZACION = "PENALIZACIÓN [ADMIN]";

    // Mensajes de validación
    public static final String ERROR_CAMPOS_VACIOS = "Los campos del formulario están incompletos";
    public static final String ERROR_TRANSACCION_NO_ENCONTRADA = "La transacción no fue encontrada";
    public static final String EXITO_AGREGAR_TRANSACCION = "La transacción fue agregada correctamente";
    public static final String ERROR_AGREGAR_TRANSACCION = "La transacción no fue agregada";
    public static final String EXITO_DEPOSITO = "El depósito se realizó correctamente";
    public static final String ERROR_DEPOSITO = "El depósito no se pudo realizar";
    public static final String EXITO_RETIRO = "El retiro se realizó correctamente";
    public static final String ERROR_RETIRO = "El retiro no se pudo realizar. Saldo insuficiente.";
    public static final String EXITO_TRANSFERENCIA = "La transferencia se realizó correctamente";
    public static final String ERROR_TRANSFERENCIA = "La transferencia no se pudo realizar. Saldo insuficiente.";

    public static final String EXITO_TRANSACCION_ADMIN = "Transacción administrativa realizada correctamente";
    public static final String ERROR_TRANSACCION_ADMIN = "Error al realizar la transacción administrativa";
    public static final String ERROR_JUSTIFICACION_VACIA = "Debe proporcionar una justificación para la transacción administrativa";
}

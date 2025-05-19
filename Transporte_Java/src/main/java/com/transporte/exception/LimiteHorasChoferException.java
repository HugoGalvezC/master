package com.transporte.exception;

public class LimiteHorasChoferException extends BusinessException {
    public LimiteHorasChoferException(int horas) {
        super("El chofer ha excedido el límite de horas: " + horas);
    }
}
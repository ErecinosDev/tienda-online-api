
package com.tiendaonline.exception;

import com.tiendaonline.entity.MetodoPago;

public class EstrategiaPagoNoEncontradaException extends RuntimeException {

    public EstrategiaPagoNoEncontradaException(MetodoPago metodo) {

        super("No existe estrategia para " + metodo);
    }
}


package com.tiendaonline.exception;

public class CodigoProductoDuplicadoException extends RuntimeException {

    public CodigoProductoDuplicadoException(String codigo) {

        super("Ya existe un producto con el código " + codigo);
    }
}

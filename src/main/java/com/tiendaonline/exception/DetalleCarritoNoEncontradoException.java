
package com.tiendaonline.exception;

public class DetalleCarritoNoEncontradoException extends RuntimeException {

    public DetalleCarritoNoEncontradoException(Integer id) {

        super("El producto " + id + " no existe en el carrito actual");
    }
}

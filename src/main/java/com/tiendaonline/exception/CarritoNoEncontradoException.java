
package com.tiendaonline.exception;

public class CarritoNoEncontradoException extends RuntimeException {

    public CarritoNoEncontradoException() {

        super("No existe un carrito para la sesión actual");
    }
}

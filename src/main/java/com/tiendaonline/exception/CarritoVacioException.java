
package com.tiendaonline.exception;

public class CarritoVacioException extends RuntimeException {

    public CarritoVacioException() {

        super("El carrito no contiene productos");
    }
}


package com.tiendaonline.exception;

public class ProductoEnUsoException extends RuntimeException {

    public ProductoEnUsoException(Integer idProducto) {

        super("No se puede eliminar el producto " + idProducto + " porque está en uso");
    }
}

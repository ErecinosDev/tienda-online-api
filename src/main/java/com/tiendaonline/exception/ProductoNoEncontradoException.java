package com.tiendaonline.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Integer idProducto) {
        super("No se encontró el producto con identificador " + idProducto);
    }
}

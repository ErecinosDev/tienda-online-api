
package com.tiendaonline.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito_compra")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarritoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Integer idCarrito;

    @Column(
            name = "codigo_anonimo",
            nullable = false,
            unique = true,
            length = 36
    )
    private String codigoAnonimo;

    @OneToMany(
            mappedBy = "carrito",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DetalleCarrito> detalles = new ArrayList<>();

    public CarritoCompra(String codigoAnonimo) {
        this.codigoAnonimo = codigoAnonimo;
    }

    public DetalleCarrito buscarDetalle(Integer productoId) {

        return detalles.stream()
                .filter(d -> d.getProducto()
                        .getIdProducto()
                        .equals(productoId))
                .findFirst()
                .orElse(null);
    }

    public void agregarDetalle(DetalleCarrito detalle) {

        detalle.asignarCarrito(this);
        detalles.add(detalle);
    }

    public void eliminarDetalle(DetalleCarrito detalle) {

        detalles.remove(detalle);
        detalle.asignarCarrito(null);
    }

    public void vaciar() {

        detalles.clear();
    }

    @Override
    public String toString() {
        return "CarritoCompra{" +
                "idCarrito=" + idCarrito +
                ", codigoAnonimo='" + codigoAnonimo + '\'' +
                ", detalles=" + detalles +
                '}';
    }
}


package com.tiendaonline.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(
        name = "detalle_carrito",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_detalle_carrito_producto",
                columnNames = {"id_carrito", "id_producto"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetalleCarrito {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_detalle")
 private Integer idDetalle;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_carrito", nullable = false)
 private CarritoCompra carrito;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_producto", nullable = false)
 private Producto producto;

 @Column(nullable = false)
 private Integer cantidad;

 @Column(
         nullable = false,
         precision = 12,
         scale = 2
 )
 private BigDecimal subtotal;

 public DetalleCarrito(Producto producto, Integer cantidad) {

  this.producto = producto;
  actualizarCantidad(cantidad);
 }

 public void asignarCarrito(CarritoCompra carrito) {

  this.carrito = carrito;
 }

 public void actualizarCantidad(Integer cantidad) {

  if (cantidad == null || cantidad <= 0) {
   throw new IllegalArgumentException(
           "La cantidad debe ser positiva"
   );
  }

  this.cantidad = cantidad;

  this.subtotal = producto.getPrecio()
          .multiply(BigDecimal.valueOf(cantidad));
 }

 public void sumarCantidad(Integer cantidad) {

  actualizarCantidad(this.cantidad + cantidad);
 }

 @Override
 public String toString() {
  return "DetalleCarrito{" +
          "idDetalle=" + idDetalle +
          ", carrito=" + carrito +
          ", producto=" + producto +
          ", cantidad=" + cantidad +
          ", subtotal=" + subtotal +
          '}';
 }
}

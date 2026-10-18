
package com.tiendaonline.entity;

import com.tiendaonline.strategy.ResultadoPago;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "compra")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCompra estado;

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    public Compra(BigDecimal t, MetodoPago m) {

        total = t;
        metodoPago = m;
        estado = EstadoCompra.INICIADA;
        fechaCreacion = Instant.now();
    }

    public void aplicarResultado(ResultadoPago r) {

        estado = r.estado();
    }

    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", total=" + total +
                ", metodoPago=" + metodoPago +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}


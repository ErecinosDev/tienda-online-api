
package com.tiendaonline.repository;

import com.tiendaonline.entity.CarritoCompra;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoCompra, Integer> {

 Optional<CarritoCompra> findByCodigoAnonimo(String codigoAnonimo);

 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("""
            SELECT c
            FROM CarritoCompra c
            LEFT JOIN FETCH c.detalles d
            LEFT JOIN FETCH d.producto
            WHERE c.codigoAnonimo = :codigo
            """)
 Optional<CarritoCompra> findByCodigoAnonimoForUpdate(
         @Param("codigo") String codigoAnonimo
 );
}

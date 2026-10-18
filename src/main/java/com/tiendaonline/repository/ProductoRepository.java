
package com.tiendaonline.repository;

import com.tiendaonline.entity.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    boolean existsByCodigo(String codigo);
}


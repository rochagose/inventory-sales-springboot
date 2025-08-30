package com.rocha.Inventario.Ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocha.Inventario.Ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

}

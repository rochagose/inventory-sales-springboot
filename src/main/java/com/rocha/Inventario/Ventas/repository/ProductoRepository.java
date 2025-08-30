package com.rocha.Inventario.Ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rocha.Inventario.Ventas.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
}

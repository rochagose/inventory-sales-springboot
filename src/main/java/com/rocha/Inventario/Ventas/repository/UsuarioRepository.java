package com.rocha.Inventario.Ventas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.rocha.Inventario.Ventas.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByUsername(String username);
}

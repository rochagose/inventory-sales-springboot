package com.rocha.Inventario.Ventas.service;

import java.util.List;

import com.rocha.Inventario.Ventas.model.Usuario;

public interface UsuarioService {
    public List<Usuario> getAllUsers();
    public void deleteUserById(Long id);
    public Usuario getUserById(Long id);
    public Usuario saveUser(Usuario user);
    public Usuario updateUser(Usuario user);
}

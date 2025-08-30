package com.rocha.Inventario.Ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocha.Inventario.Ventas.model.Usuario;
import com.rocha.Inventario.Ventas.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario saveUser(Usuario user) {
        return usuarioRepository.save(user);
    }

    @Override
    public Usuario updateUser(Usuario user) {
        return usuarioRepository.save(user);
    }

}

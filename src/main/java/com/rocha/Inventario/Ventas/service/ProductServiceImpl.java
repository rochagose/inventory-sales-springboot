package com.rocha.Inventario.Ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocha.Inventario.Ventas.model.Producto;
import com.rocha.Inventario.Ventas.repository.ProductoRepository;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> getAllProducts() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public Producto saveProduct(Producto product) {
        product.setIndicador(calculateIndicator(product.getExistencia()));
        return productoRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public Producto updateProduct(String code, Producto product) {
        return null;
    }

    @Override
    public String calculateIndicator(Double existencia) {
        if (existencia == null) return "Sin datos";
        if (existencia <= 5) return "Bajo";
        if (existencia <= 20) return "Medio";
        return "Alto";
    }

    
}

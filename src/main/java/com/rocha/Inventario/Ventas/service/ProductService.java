package com.rocha.Inventario.Ventas.service;

import java.util.List;


import com.rocha.Inventario.Ventas.model.Producto;

public interface ProductService {
    public List<Producto> getAllProducts();
    public Producto getProductById(Long id);
    public Producto saveProduct(Producto product);
    public void deleteProduct(Long id);
    public Producto updateProduct(String code, Producto product);
    public String calculateIndicator(Double existencia);
}

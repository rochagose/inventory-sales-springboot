package com.rocha.Inventario.Ventas.service;

import java.util.List;

import com.rocha.Inventario.Ventas.model.Venta;

public interface VentaService {
    public List<Venta> getAllVentas();
    public Venta registrarVenta(Venta venta);
}

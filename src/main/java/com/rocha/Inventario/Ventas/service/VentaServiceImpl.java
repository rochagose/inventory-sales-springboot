package com.rocha.Inventario.Ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocha.Inventario.Ventas.model.Venta;
import com.rocha.Inventario.Ventas.repository.VentaRepository;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta registrarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

}

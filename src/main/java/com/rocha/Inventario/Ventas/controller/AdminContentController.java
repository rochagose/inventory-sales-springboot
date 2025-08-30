package com.rocha.Inventario.Ventas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rocha.Inventario.Ventas.model.Producto;
import com.rocha.Inventario.Ventas.model.Usuario;
import com.rocha.Inventario.Ventas.model.Venta;
import com.rocha.Inventario.Ventas.service.ProductService;
import com.rocha.Inventario.Ventas.service.UsuarioService;
import com.rocha.Inventario.Ventas.service.VentaService;

@Controller
@RequestMapping("/admin")
public class AdminContentController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private VentaService ventaService;
    @Autowired
    private PasswordEncoder encoder;

    /* MENU */

    @GetMapping({ "", "/" })
    public String redirectToMenu() {
        return "redirect:admin_menu";
    }

    @GetMapping("/menu")
    public String adminMenu() {
        return "admin_menu";
    }

    /* GESTION DE USUARIOS */

    @GetMapping("/menu/usuarios")
    public String adminUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.getAllUsers());
        model.addAttribute("usuario", new Usuario());
        return "admin_usuarios";
    }

    @PostMapping("/menu/usuarios/registrar")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        usuarioService.saveUser(usuario);
        return "redirect:/admin/menu/usuarios";
    }

    @RequestMapping(value = "/menu/usuarios/actualizar", method = { RequestMethod.POST, RequestMethod.PUT })
    public String actualizarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        Usuario user = usuarioService.getUserById(usuario.getId());
        user.setNombre(usuario.getNombre());
        user.setApellidoPaterno(usuario.getApellidoPaterno());
        user.setApellidoMaterno(usuario.getApellidoMaterno());
        user.setEmail(usuario.getEmail());
        user.setTelefono(usuario.getTelefono());
        user.setRol(usuario.getRol());
        usuarioService.saveUser(user);

        return "redirect:/admin/menu/usuarios";
    }

    @GetMapping("/menu/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteUserById(id);
        return "redirect:/admin/menu/usuarios";
    }

    /* GESTION DE PRODUCTOS */

    @GetMapping("/menu/productos")
    public String adminProductos(Model model) {
        model.addAttribute("productos", productService.getAllProducts());
        model.addAttribute("producto", new Producto());
        return "admin_productos";
    }

    @PostMapping("/menu/productos/registrar")
    public String registrarProducto(@ModelAttribute("producto") Producto producto, Model model) {
        productService.saveProduct(producto);
        return "redirect:/admin/menu/productos";
    }

    @RequestMapping(value = "/menu/productos/actualizar", method = { RequestMethod.POST, RequestMethod.PUT })
    public String actualizarProducto(@ModelAttribute("producto") Producto producto, Model model) {
        Producto prod = productService.getProductById(producto.getId());
        prod.setCodigo(producto.getCodigo());
        prod.setNombre(producto.getNombre());
        prod.setPrecio(producto.getPrecio());
        prod.setUnidadMedida(producto.getUnidadMedida());
        prod.setExistencia(producto.getExistencia());
        productService.saveProduct(prod);
        return "redirect:/admin/menu/productos";
    }

    @GetMapping("/menu/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/menu/productos";
    }

    @GetMapping("/menu/ventas")
    public String adminVentas(Model model) {
        model.addAttribute("ventas", ventaService.getAllVentas());
        return "ventas";
    }

    @GetMapping("/menu/pos")
    public String posPage(Model model, @AuthenticationPrincipal Usuario user) {
        model.addAttribute("productos", productService.getAllProducts());
        return "pos";
    }

    @PostMapping("/menu/pos/registrar")
    @ResponseBody
    public ResponseEntity<?> registrarVenta(@RequestBody List<Map<String, Object>> articulos) {
        System.out.println("Articulos recibidos: " + articulos);
        for (Map<String, Object> item : articulos) {
            try {
                Long productoId = Long.valueOf(item.get("productoId").toString());
                Double cantidad = Double.valueOf(item.get("cantidad").toString());
                Double total = Double.valueOf(item.get("total").toString());

                Producto p = productService.getProductById(productoId);
                if (p == null) {
                    System.out.println("Producto no encontrado: " + productoId);
                    continue;
                }

                // ✅ Actualizar stock
                double nuevaExistencia = p.getExistencia() - cantidad;
                if (nuevaExistencia < 0) {
                    System.out.println("No hay suficiente stock para el producto: " + p.getNombre());
                    continue; // o lanzar excepción según tu lógica
                }
                p.setExistencia(nuevaExistencia);
                productService.saveProduct(p);

                // Registrar la venta
                Venta v = new Venta();
                v.setProducto(p);
                v.setCantidad(cantidad);
                v.setTotal(total);
                v.setFecha(new java.sql.Date(System.currentTimeMillis()));
                ventaService.registrarVenta(v);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().build();
    }

    /* CERRAR SESION */

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

}

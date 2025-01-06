package com.bestforpets.Service.Carrito;

import com.bestforpets.Model.Carrito.Carrito;
import com.bestforpets.Model.Carrito.ItemCarrito;
import com.bestforpets.Model.Productos.Productos;
import com.bestforpets.Model.Usuarios.Usuarios;
import com.bestforpets.Repository.Carrito.CarritoRepository;
import com.bestforpets.Repository.Productos.ProductosRepository;
import com.bestforpets.Repository.Usuarios.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductosRepository productosRepository;
    private final UsuariosRepository usuariosRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          ProductosRepository productosRepository,
                          UsuariosRepository usuariosRepository) {
        this.carritoRepository = carritoRepository;
        this.productosRepository = productosRepository;
        this.usuariosRepository = usuariosRepository;
    }

    public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        Usuarios usuario = usuariosRepository.findById(usuarioId).orElseThrow();
        Productos producto = productosRepository.findById(productoId).orElseThrow();

        Carrito carrito = carritoRepository.findByUsuario(usuario).orElseGet(() -> {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            return nuevoCarrito;
        });

        if (carrito.getItems() == null) {
            carrito.setItems(new ArrayList<>());
        }

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getIdProduct().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            carrito.getItems().add(nuevoItem);
        }

        return carritoRepository.save(carrito);
    }

    public Carrito obtenerCarrito(Long usuarioId) {
        Usuarios usuario = usuariosRepository.findById(usuarioId).orElseThrow();
        return carritoRepository.findByUsuario(usuario).orElseGet(() -> {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            return nuevoCarrito;
        });
    }

    public void eliminarProductoDelCarrito(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow();

        boolean productoEliminado = carrito.getItems().removeIf(item -> item.getProducto().getIdProduct().equals(productoId));

        if (productoEliminado) {
            carritoRepository.save(carrito);
        }
    }
}

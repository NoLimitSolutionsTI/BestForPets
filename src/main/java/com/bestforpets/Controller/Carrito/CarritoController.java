package com.bestforpets.Controller.Carrito;

import com.bestforpets.Model.Carrito.Carrito;
import com.bestforpets.Service.Carrito.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/agregar")
    public ResponseEntity<Carrito> agregarProductoAlCarrito(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId,
            @RequestParam int cantidad) {

        Carrito carrito = carritoService.agregarProducto(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(carrito);
    }

    // Endpoint para obtener el carrito de un usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long usuarioId) {
        Carrito carrito = carritoService.obtenerCarrito(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    // Endpoint para eliminar un producto del carrito
    @DeleteMapping("/eliminar/{carritoId}/producto/{productoId}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @PathVariable Long carritoId,
            @PathVariable Long productoId) {

        carritoService.eliminarProductoDelCarrito(carritoId, productoId);
        return ResponseEntity.noContent().build();
    }
}

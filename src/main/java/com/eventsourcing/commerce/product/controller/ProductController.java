package com.eventsourcing.commerce.product.controller;

import com.eventsourcing.commerce.product.ProductService;
import com.eventsourcing.commerce.product.command.*;
import com.eventsourcing.commerce.product.controller.dto.RegisterProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> registerProduct(@RequestBody RegisterProductRequest registerProductRequest){
        UUID productId = productService.registerProduct(registerProductRequest);
        return ResponseEntity.created(URI.create("/products/" + productId)).build();
    }

    @PostMapping("{productId}/stock/add")
    public ResponseEntity<Void> addStock(@PathVariable UUID productId,
                                         @RequestBody AddStock addStock){
        productService.addStock(addStock, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{productId}/stock/deduct")
    public ResponseEntity<Void> deductStock(@PathVariable UUID productId, @RequestBody DeductStock deductStock){
        productService.deductStock(deductStock, productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{productId}/discontinue")
    public ResponseEntity<Void> discontinueProduct(@PathVariable UUID productId,
                                                   @RequestBody DiscontinueProduct discontinueProduct){
        productService.discontinueProduct(discontinueProduct, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{productId}/stock/release")
    public ResponseEntity<Void> releaseStock(@PathVariable UUID productId, @RequestBody ReleaseStock releaseStock){
        productService.releaseStock(releaseStock, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{productId}/stock/reserve")
    public ResponseEntity<Void> reserveStock(@PathVariable UUID productId, @RequestBody ReserveStock reserveStock){
        productService.reserveStock(reserveStock, productId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID productId, @RequestBody UpdateProduct updateProduct){
        productService.updateProduct(updateProduct, productId);
        return ResponseEntity.ok().build();
    }
}

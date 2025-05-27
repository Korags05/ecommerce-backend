package org.kunal.ECommerce.product.controller;

import org.kunal.ECommerce.common.ApiResponse;
import org.kunal.ECommerce.product.model.Product;
import org.kunal.ECommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully", product));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Product not found", null));
        }
    }


    @PostMapping("/product")
    public ResponseEntity<ApiResponse<Product>> addProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Product saved = productService.addOrUpdateProduct(product, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Product added successfully", saved));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/product")
    public ResponseEntity<ApiResponse<String>> updateProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Product updatedProduct = null;
        try {
            updatedProduct = productService.addOrUpdateProduct(product, image);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "Product updated successfully", null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Failed to update product", null));
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Product product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "Product deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Failed to delete product", null));
        }
    }

    @GetMapping("/product/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam String keyword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Product> products = productService.searchProduct(keyword);
        if (products!=null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "Product fetched successfully", products));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Failed to fetch product", null));
        }
    }

}

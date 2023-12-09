package com.sook.Task.service;

import com.sook.Task.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    List<Product> searchProducts(String category, Double minPrice, Double maxPrice);

    void addProduct(Product product);

    void updateProduct(Long id, Product updatedProduct);

    void deleteProduct(Long id);

    List<Product> getProductsByCategory(String category);
}
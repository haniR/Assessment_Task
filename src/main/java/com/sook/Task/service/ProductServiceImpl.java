package com.sook.Task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sook.Task.model.Product;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> products;

    public ProductServiceImpl() {
        // Load products from the JSON file during service instantiation
        this.products = loadProductsFromFile("products.json");
    }

    // load products from a JSON file
    private List<Product> loadProductsFromFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> productList = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                productList = objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately in a production environment
        }

        return productList;
    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        // Find and return a product by ID, or null if not found
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Product> searchProducts(String category, Double minPrice, Double maxPrice) {
        // Filter products based on category and price range
        return products.stream()
                .filter(product -> (category == null || product.getCategory().equalsIgnoreCase(category)) &&
                        (minPrice == null || product.getPrice() >= minPrice) &&
                        (maxPrice == null || product.getPrice() <= maxPrice))
                .collect(Collectors.toList());
    }

    @Override
    public void addProduct(Product product) {
        Long newId = findLastUsedId() + 1;
        product.setId(newId);
        // Add a new product to the list
        products.add(product);
    }

    private Long findLastUsedId() {
        return products.stream()
                .mapToLong(Product::getId)
                .max()
                .orElse(0L);
    }

    @Override
    public void updateProduct(Long id, Product updatedProduct) {
        // Update an existing product by ID
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    @Override
    public void deleteProduct(Long id) {
        // Delete a product by ID
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId().equals(id)) {
                iterator.remove();
                return;
            }
        }
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // Get products filtered by category
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}

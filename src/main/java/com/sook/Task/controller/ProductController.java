package com.sook.Task.controller;

import com.sook.Task.model.Product;
import com.sook.Task.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable @NotNull Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Add a new product
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = getResponseEntity(bindingResult);
        if (errorResponse != null) return errorResponse;

        productService.addProduct(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Update an existing product by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid Product updatedProduct,
            BindingResult bindingResult) {
        ResponseEntity<?> errorResponse = getResponseEntity(bindingResult);
        if (errorResponse != null) return errorResponse;

        Product existingProduct = productService.getProductById(id);
        if (existingProduct != null) {
            updateFields(existingProduct, updatedProduct);
            productService.updateProduct(id, existingProduct);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void updateFields(Product existingProduct, Product updatedProduct) {
        // Get all fields of the Product class
        Field[] fields = Product.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                // Make the field accessible (even if it's private)
                field.setAccessible(true);

                // Get the value of the field in the updatedProduct
                Object value = field.get(updatedProduct);

                // If the value is not null, update the field in existingProduct
                if (value != null) {
                    field.set(existingProduct, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // handle validation errors
    private ResponseEntity<?> getResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errors);

            return ResponseEntity.badRequest().body(errorResponse);
        }
        return null;
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct != null) {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Search for products based on category and/or price range
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) String minPrice,
            @RequestParam(name = "maxPrice", required = false) String maxPrice) {
        if (category != null) {
            category = category.trim().toLowerCase();
        }

        Double parsedMinPrice = null;
        Double parsedMaxPrice = null;

        if (minPrice != null) {
            parsedMinPrice = parseDouble(minPrice);
            if (parsedMinPrice == null) {
                return ResponseEntity.badRequest().body("Invalid minPrice. Please provide a numeric value.");
            }
        }

        if (maxPrice != null) {
            parsedMaxPrice = parseDouble(maxPrice);
            if (parsedMaxPrice == null) {
                return ResponseEntity.badRequest().body("Invalid maxPrice. Please provide a numeric value.");
            }
        }

        List<Product> result = productService.searchProducts(category, parsedMinPrice, parsedMaxPrice);
        return ResponseEntity.ok(result);
    }

    // Aggregate data for a specific category
    @GetMapping("/aggregate/{category}")
    public ResponseEntity<Map<String, Double>> aggregateCategoryData(@PathVariable String category) {
        List<Product> productsInCategory = productService.getProductsByCategory(category);

        if (productsInCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        double averagePrice = calculateAveragePrice(productsInCategory);
        int totalQuantity = calculateTotalQuantity(productsInCategory);

        Map<String, Double> result = new HashMap<>();
        result.put("averagePrice", averagePrice);
        result.put("totalQuantity", (double) totalQuantity);

        return ResponseEntity.ok(result);
    }

    // calculate average price
    private double calculateAveragePrice(List<Product> products) {
        double sum = 0;
        for (Product product : products) {
            sum += product.getPrice();
        }
        return sum / products.size();
    }

    // calculate total quantity
    private int calculateTotalQuantity(List<Product> products) {
        int totalQuantity = 0;
        for (Product product : products) {
            totalQuantity += product.getQuantity();
        }
        return totalQuantity;
    }

    // parse a string to double
    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

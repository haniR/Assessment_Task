package com.sook.Task;

import com.sook.Task.controller.ProductController;
import com.sook.Task.model.Product;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private ProductController productController;

    // Test to verify that the endpoint for retrieving all products returns a 200 status code
    @Test
    public void testGetAllProducts() {
        given()
                .standaloneSetup(productController)
                .when()
                .get("/api/products")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    // Test to verify that a new product can be successfully added
    @Test
    public void testAddProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setCategory("Test");
        newProduct.setPrice(10.0);
        newProduct.setQuantity(20);

        given()
                .standaloneSetup(productController)
                .contentType(ContentType.JSON)
                .body(newProduct)
                .when()
                .post("/api/products")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }
}
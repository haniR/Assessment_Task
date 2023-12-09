# Assessment Task - RESTful API Development

## Overview

This project implements a RESTful API for processing data from a JSON file containing product records. The API provides functionalities for data manipulation and retrieval based on specific criteria. The project is developed using Java and the Spring Boot framework.

## Table of Contents

- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Data Processing](#data-processing)
- [Data Aggregation](#data-aggregation)
- [Data Modification](#data-modification)
- [Data Validation](#data-validation)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Getting Started

### Prerequisites

- Java (version X.X.X)
- [Maven](https://maven.apache.org/install.html)
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/haniR/Assessment_Task.git
    ```

2. Navigate to the project directory:

    ```bash
    cd Assessment_Task
    ```

3. Build the project:

    ```bash
    ./mvnw clean install
    ```

4. Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

The application should now be running locally. You can access the API at `http://localhost:8080/api/products`.

## API Endpoints

### GET /api/products

Retrieve a list of all products.

### GET /api/products/{id}

Retrieve a product by its ID.

### POST /api/products

Add a new product.

### PUT /api/products/{id}

Update an existing product.

### DELETE /api/products/{id}

Delete a product by its ID.

### GET /api/products/search
### GET /api/products/search?categroy=tools
### GET /api/products/search?categroy=tools&minPrice=10&maxPrice=40
Search for products based on category and/or price range.

### GET /api/products/aggregate/{category}

Get aggregated data: average price and total quantity of products in a category.

## Data Processing

The application reads product data from a JSON file (`products.json`) and loads it into an in-memory data structure during startup.

## Data Aggregation

The API provides endpoints to aggregate data, including average price and total quantity in a category.

## Data Modification

Endpoints are available for adding, updating, and deleting product records.

## Data Validation

All data inputs for new and updated records are validated to ensure data integrity.

## Testing

Integration tests are implemented using RestAssured. To run the tests:

```bash
./mvnw test

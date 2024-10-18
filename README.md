# SpringShop

SpringShop is a Spring Boot application that provides a comprehensive platform for customers to manage their favorite products, recent searches, and other functionalities. The application leverages the power of Spring Boot to create a robust and scalable backend, ensuring seamless user experiences. It utilizes PostgreSQL as its database, offering reliable and efficient data storage and retrieval. The application includes features such as user registration and authentication, product management, and profile management, making it a versatile solution for e-commerce needs.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Setup](#setup)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Customer](#customer)
  - [Products](#products)
  - [Favourites](#favourites)
  - [Recently Viewed](#recently-viewed)
- [Contributing](#contributing)
- [License](#license)

## Features

- User registration and authentication
- Manage favorite products
- View recently viewed products
- Search products with filters
- Manage user profile (username, password, email, mobile number)

## Technologies
Make sure you have these installed and setup before running the application

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven

## Setup

1. **Clone the repository:**

    ```bash
    git clone https://github.com/kaktus011/SpringShop.git
    cd SpringShop
    ```

2. **Configure the database:**

    Update the database configuration in `src/main/resources/application.properties`:

    ```ini
    spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
    spring.datasource.username=your_postgres_username
    spring.datasource.password=your_postgres_password
    ```

3. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

## Usage
Access the application at http://localhost:8080 and test the different endpoints from Postman, or access from http://localhost:8080/swagger-ui.html and test from Swagger. Once the customer account is created and logged in, ensure to use the received token for accessing the rest of the functionality.

## API Endpoints

### Authentication

- **POST /register**: Register a new user
  - **Request Body:**
    ```json
    {
      "username": "string",
      "password": "string",
      "email": "string",
      "mobileNumber": "string",
      "name": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "id": "long",
      "username": "string",
      "email": "string",
      "mobileNumber": "string",
      "name": "string"
    }
    ```

- **POST /login**: Authenticate a user and return a JWT token
  - **Request Body:**
    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "token": "string"
    }
    ```

### Customer

- **GET /customer/details**: Get customer details
  - **Response:**
    ```json
    {
      "username": "string",
      "email": "string",
      "name": "string",
      "mobileNumber": "string"
    }
    ```

- **POST /customer/change-username**: Change username
  - **Request Body:**
    ```json
    {
      "oldUsername": "string",
      "newUsername": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "username": "string",
      "email": "string",
      "name": "string",
      "mobileNumber": "string"
    }
    ```

- **POST /customer/change-password**: Change password
  - **Request Body:**
    ```json
    {
      "oldPassword": "string",
      "newPassword": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "username": "string",
      "email": "string",
      "name": "string",
      "mobileNumber": "string"
    }
    ```

- **POST /customer/change-email**: Change email
  - **Request Body:**
    ```json
    {
      "oldEmail": "string",
      "newEmail": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "username": "string",
      "email": "string",
      "name": "string",
      "mobileNumber": "string"
    }
    ```

- **POST /customer/change-mobile-number**: Change mobile number
  - **Request Body:**
    ```json
    {
      "oldMobileNumber": "string",
      "newMobileNumber": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "username": "string",
      "email": "string",
      "name": "string",
      "mobileNumber": "string"
    }
    ```

### Products

- **GET /products**: Get filtered products
  - **Request Parameters:**
    - `categoryName` (optional)
    - `minPrice` (optional)
    - `maxPrice` (optional)
    - `location` (optional)
    - `searchTerm` (optional)
    - `page` (optional, default: 0)
    - `size` (optional, default: 10)
  - **Response:**
    ```json
    {
      "content": [
        {
          "id": "long",
          "title": "string",
          "description": "string",
          "price": "double",
          "category": "string",
          "imageUrl": "string",
          "status": "string",
          "location": "string",
          "view": "int",
          "customerId": "long",
          "customerName": "string",
          "customerMobileNumber": "string",
          "customerEmail": "string",
          "favouritesCount": "int"
        }
      ],
      "pageable": {
        "sort": {
          "sorted": "boolean",
          "unsorted": "boolean",
          "empty": "boolean"
        },
        "pageNumber": "int",
        "pageSize": "int",
        "offset": "long",
        "paged": "boolean",
        "unpaged": "boolean"
      },
      "totalPages": "int",
      "totalElements": "long",
      "last": "boolean",
      "size": "int",
      "number": "int",
      "sort": {
        "sorted": "boolean",
        "unsorted": "boolean",
        "empty": "boolean"
      },
      "numberOfElements": "int",
      "first": "boolean",
      "empty": "boolean"
    }
    ```

- **GET /products/{id}**: Get product details
  - **Response:**
    ```json
    {
      "id": "long",
      "title": "string",
      "description": "string",
      "price": "double",
      "categoryId": "long",
      "categoryName": "string",
      "imageUrl": "string",
      "status": "string",
      "location": "string",
      "view": "int",
      "customerId": "long",
      "customerName": "string",
      "customerMobileNumber": "string",
      "customerEmail": "string",
      "favouritesCount": "int"
    }
    ```

- **POST /products**: Create a new product
  - **Request Body:**
    ```json
    {
      "title": "string",
      "description": "string",
      "price": "double",
      "category": "string",
      "image": "string",
      "status": "string",
      "location": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "id": "long",
      "title": "string",
      "description": "string",
      "price": "double",
      "category": "string",
      "imageUrl": "string",
      "status": "string",
      "location": "string",
      "view": "int",
      "customerId": "long",
      "customerName": "string",
      "customerMobileNumber": "string",
      "customerEmail": "string",
      "favouritesCount": "int"
    }
    ```

- **PUT /products/{id}**: Update a product
  - **Request Body:**
    ```json
    {
      "title": "string",
      "description": "string",
      "price": "double",
      "category": "string",
      "image": "string",
      "status": "string",
      "location": "string"
    }
    ```
  - **Response:**
    ```json
    {
      "id": "long",
      "title": "string",
      "description": "string",
      "price": "double",
      "category": "string",
      "imageUrl": "string",
      "status": "string",
      "location": "string",
      "view": "int",
      "customerId": "long",
      "customerName": "string",
      "customerMobileNumber": "string",
      "customerEmail": "string",
      "favouritesCount": "int"
    }
    ```

- **DELETE /products/{id}**: Deactivate a product
  - **Response:**
    ```json
    {
      "message": "Product deactivated successfully"
    }
    ```

### Favourites

- **GET /favourites/get-favourite-products**: Get favorite products
  - **Response:**
    ```json
    [
      {
        "id": "long",
        "title": "string",
        "description": "string",
        "price": "double",
        "category": "string",
        "imageUrl": "string",
        "status": "string",
        "location": "string",
        "view": "int",
        "customerId": "long",
        "customerName": "string",
        "customerMobileNumber": "string",
        "customerEmail": "string",
        "favouritesCount": "int"
      }
    ]
    ```

- **POST /favourites/make-product-favourite**: Add a product to favorites
  - **Request Body:**
    ```json
    {
      "productId": "long"
    }
    ```
  - **Response:**
    ```json
    {
      "message": "Product added to favourites successfully"
    }
    ```

- **DELETE /favourites/delete-favourite-product**: Remove a product from favorites
  - **Request Body:**
    ```json
    {
      "productId": "long"
    }
    ```
  - **Response:**
    ```json
    {
      "message": "Product removed from favourites successfully"
    }
    ```

### Recently Viewed

- **GET /recently-viewed/get-recent-products**: Get recently viewed products
  - **Response:**
    ```json
    [
      {
        "id": "long",
        "title": "string",
        "description": "string",
        "price": "double",
        "category": "string",
        "imageUrl": "string",
        "status": "string",
        "location": "string",
        "view": "int",
        "customerId": "long",
        "customerName": "string",
        "customerMobileNumber": "string",
        "customerEmail": "string",
        "favouritesCount": "int"
      }
    ]
    ```

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any changes.

## License

This project is licensed under the MIT License.

# Store Service
This service is designed to provide a full set of features for managing products, user carts, checkout and special deals per product.

## Getting Started

You can start the service in two ways:

1. **Docker:** `docker-compose up`
2. **Gradle (Java 17 is a prerequisite):** `./gradlew run`

The service starts at port 8080.

To run tests, you can also use two methods:

**Gradle (Java 17 is a prerequisite):** `./gradlew test`

## API Docs
You can find API endpoints after starting up a service at this path:
http://localhost:8080/swagger-ui/index.html

Note: some endpoints are secured, since involved authorities, so you have to attach a jwt token which you get after authorization.
Attach it to `Authorization` header like `Bearer <jwt>`. More information below.

## Features

### JWT Authentication
This service uses JSON Web Token (JWT) for authentication, ensuring secure communication between the client and the server.

### Optimistic Locking
To ensure data integrity and concurrent safeness, our service utilizes the Optimistic Locking technique. 
This approach prevents conflicts and data inconsistencies when multiple transactions try to modify the same data simultaneously.

Note: Locking is applied to update product & stock only, since several admins can do that in the same time,
however for cart, orders, user details it is assumed that only 1 user is updating details. 
In real world such type of locking should be applied to them as well.

### Transactional Safety
The service leverages the `@Transactional` annotation from Spring Framework to manage database transactions. 
This ensures that all database transactions are performed in the correct order, providing a consistent state and preventing data inconsistencies.

### Admin Endpoints
As an administrator, you have access to several endpoints to manage products:

1. **Create a Product:** Add a new product to the inventory.
2. **Update a Product:** Modify the details of an existing product.
3. **Update Product Stock:** Adjust the stock level of a specific product.
4. **Delete a Product:** Remove a product from the stock.

### User Endpoints
As a user, you can interact with the service through the following endpoints:

1. **Add a Product to Cart:** Include a product in your shopping cart.
2. **Delete a Product from Cart:** Remove a product from your shopping cart.
3. **Checkout:** Complete the purchase of all items in your cart.

### Default users
Admin rights: username=`admin`, password=`admin`
User rights: username=`user`, password=`user`

### Deals
Service offers three types of special deals:

1. **BUY_N_GET_M_FREE:** For example, buy 2 products, and get 1 free. This deal allows you to receive free products based on the quantity of your purchase.
2. **BUY_N_GET_DISCOUNT:** For example, buy 2 products and get 50% off. This deal provides a percentage discount on the total price when you buy a certain quantity of a product.
3. **BUY_N_GET_DISCOUNT_ON_N_PLUS_1:** For example, buy 2 and get 50% off the third. This deal offers a percentage discount on the additional product when you buy a certain quantity of a product.

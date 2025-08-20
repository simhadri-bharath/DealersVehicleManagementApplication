# Dealer & Vehicle Management API

A comprehensive RESTful API built with Spring Boot for managing car dealers, their vehicle inventory, and subscription payments. This project demonstrates a secure, robust, and well-structured backend system adhering to modern best practices.

## Features

-   **Authentication & Security:** Secure REST endpoints using JWT (JSON Web Tokens) and Spring Security.
-   **CRUD Operations:** Full Create, Read, Update, Delete functionality for Dealers and Vehicles.
-   **Asynchronous Processing:** A simulated payment gateway that processes subscription payments asynchronously without blocking the API.
-   **Custom Business Logic:** A dedicated endpoint to fetch vehicles belonging only to `PREMIUM` dealers.
-   **Robust Error Handling:** Centralized exception handling for clean, standardized error responses (e.g., 404 Not Found, 400 Bad Request, 409 Conflict).
-   **Configuration Management:** Secure handling of sensitive data (database credentials, JWT secret) using a `.env` file.
-   **API Documentation:** Interactive API documentation provided via Swagger (OpenAPI 3).

## Technologies Used

-   **Framework:** Spring Boot 3
-   **Language:** Java 17
-   **Security:** Spring Security 6
-   **Data Persistence:** Spring Data JPA / Hibernate
-   **Database:** MySQL (Configured) & PostgreSQL (Ready)
-   **Build Tool:** Apache Maven
-   **API Documentation:** SpringDoc OpenAPI (Swagger UI)
-   **Environment Variables:** spring-dotenv

---

## Prerequisites

Before you begin, ensure you have the following installed:
-   JDK 17 or later
-   Apache Maven 3.6+
-   MySQL Server 8.0+ (or another compatible database)
-   A Git client

## Setup & Configuration

Follow these steps to get the project running locally.

**1. Clone the Repository**
```bash
git clone https://github.com/your-github-username/your-repo-name.git
cd your-repo-name
```

**2. Database Setup**
-   Open your MySQL client (e.g., MySQL Workbench, DBeaver).
-   Create a new database schema. The default expected name is `dvm`.
    ```sql
    CREATE DATABASE dvm;
    ```
-   The application will automatically create the necessary tables on first run (`ddl-auto=update`).

**3. Configure Environment Variables (CRITICAL STEP)**
This project uses a `.env` file to manage sensitive configuration. You must create this file.
-   In the root directory of the project, create a new file named `.env`.
-   Copy the content below into your new `.env` file and **update the values** with your local database credentials.

```
# Local Development Environment Variables

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/dvm
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

# JWT Configuration (You can leave this default for local testing)
JWT_SECRET=this-is-my-super-secret-key-for-local-development-only-12345
JWT_EXPIRATION=3600000
```
**Note:** The `.env` file is listed in `.gitignore` and should **never** be committed to version control.

---

## Running the Application

You can run the application in two ways:

**1. Using your IDE (Recommended)**
-   Import the project as a Maven project into your IDE (Spring Tool Suite, IntelliJ IDEA).
-   Locate the `DealersVehicleManagementApplication.java` file.
-   Right-click and select "Run As" -> "Java Application".

**2. Using Maven Command Line**
-   Open a terminal in the project's root directory.
-   Run the following command:
    ```bash
    mvn spring-boot:run
    ```
The application will start on `http://localhost:8080`.

---

## API Documentation & Usage

### Interactive Documentation (Swagger UI)

Once the application is running, you can access the interactive Swagger UI to explore and test all the API endpoints.

**URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### General Workflow to Test Protected Endpoints

1.  **Register a Dealer:** Use the `POST /api/auth/register` endpoint to create a new user.
2.  **Login:** Use the `POST /api/auth/login` endpoint with the new credentials.
3.  **Copy JWT Token:** From the login response, copy the `token` value.
4.  **Authorize in Swagger:**
    -   Click the green "Authorize" button at the top right of the Swagger UI.
    -   In the dialog box, paste your token into the "value" field.
    -   Click "Authorize" and then "Close".
5.  You can now successfully test all the protected (locked) endpoints.

---

## API Endpoints

### Authentication (`/api/auth`)

| Method | Endpoint             | Description                | Protected |
|--------|----------------------|----------------------------|-----------|
| `POST` | `/register`          | Registers a new dealer.    | No        |
| `POST` | `/login`             | Authenticates and gets JWT.| No        |

**Sample Register Body:**
```json
{
  "name": "Prestige Worldwide Auto",
  "email": "premium@dealer.com",
  "password": "password123",
  "subscriptionType": "PREMIUM"
}
```

### Vehicles (`/api/vehicles`)

| Method | Endpoint             | Description                                    | Protected |
|--------|----------------------|------------------------------------------------|-----------|
| `POST` | `?dealerId={id}`     | Creates a new vehicle for a specific dealer.   | Yes       |
| `GET`  | `/`                  | Retrieves a list of all vehicles.              | Yes       |
| `GET`  | `/{id}`              | Retrieves a specific vehicle by its ID.        | Yes       |
| `PUT`  | `/{id}`              | Updates an existing vehicle.                   | Yes       |
| `DELETE`| `/{id}`             | Deletes a vehicle by its ID.                   | Yes       |
| `GET`  | `/premium-dealers`   | **(Task Requirement)** Gets all vehicles of `PREMIUM` dealers. | Yes |

**Sample Create Vehicle Body (`POST /api/vehicles?dealerId=1`):**
```json
{
  "model": "Tesla Model S",
  "price": 75000,
  "status": "AVAILABLE"
}
```

### Payments (`/api/payment`)

| Method | Endpoint             | Description                                    | Protected |
|--------|----------------------|------------------------------------------------|-----------|
| `POST` | `/initiate`          | Simulates initiating a payment (returns `PENDING` status). | Yes |
| `GET`  | `/status/{paymentId}`| Polls for the final status of a payment (`SUCCESS` after 5s). | Yes |

**Sample Initiate Payment Body:**
```json
{
  "dealerId": 1,
  "amount": 99.99,
  "method": "CARD"
}
```

### Error Handling

The API provides structured error responses.

**Example 404 Not Found:**```json
{
  "timestamp": "2025-08-20T18:30:00.123",
  "status": 404,
  "error": "Not Found",
  "message": "Vehicle not found with id: 999",
  "path": "/api/vehicles/999"
}
```

**Example 400 Validation Error:**
```json
{
  "timestamp": "2025-08-20T18:32:00.543",
  "status": 400,
  "error": "Validation Failed",
  "path": "/api/auth/register",
  "messages": {
    "password": "Password must be at least 8 characters long",
    "email": "Email should be valid"
  }
}
```

---

## Author

-   **Name:** [Simhadri Bharath]
-   **GitHub:** [https://github.com/simhadri-bharath]

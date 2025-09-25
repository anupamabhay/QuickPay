# **QuickPay E-commerce Platform**

## **1. Core Objective**

The primary goal is to build a full-stack e-commerce platform named **QuickPay**. The project's main focus is to implement and master a robust, secure, and professional end-to-end payment processing workflow, mirroring real-world systems. The focus is on the payment flow, not on building a feature-rich retail platform.

## **2. Technology Stack**

  - **Backend:** Java (JDK 17+) with Spring Boot 3.x
  - **Frontend:** React
  - **Database:** PostgreSQL
  - **Payment Gateway:** Stripe
  - **Containerization:** Docker

## **3. System Architecture**

The application uses a standard 3-tier architecture:

1.  **Frontend Layer (React):** The client-side user interface responsible for presentation and user interaction.
2.  **Backend Layer (Java Spring Boot):** A REST API server that handles all business logic, data processing, and communication with the database and payment gateway.
3.  **Database Layer (PostgreSQL):** The persistent storage for all application data, including users, products, and orders.
4.  **External Integration (Stripe):** A third-party service for securely processing payments.

## **4. Database Schema**

```sql
CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES Users(id),
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status VARCHAR(50) NOT NULL, -- e.g., 'PENDING', 'COMPLETED', 'FAILED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

## **5. Core Feature Modules**

### **User Management Module**

  - [x] Implement user registration with name, email, and password.
  - [x] Implement secure user login/authentication using Spring Security.
  - [x] Hash all user passwords using BCrypt.
  - [x] Manage user sessions, preferably using JWT tokens.

### **Product Catalog Module**

  - [x] Create REST API endpoints to list products.
  - [x] Display products in the frontend with a name, description, and price.
  - [x] **Constraint:** The product list can be hardcoded or loaded from a simple data file on the backend initially. A complex inventory management system is not required.

### **Shopping Cart Module**

  - [x] Allow users to add and remove products from a shopping cart.
  - [x] Ensure the cart state persists across user sessions.
  - [x] Automatically calculate the cart's total amount.
  - [x] Manage cart state efficiently on the frontend using a tool like React Context or Redux.

### **Payment & Order Processing Module**

  - [x] Create an API endpoint for order creation. An order should be created in the database with a `PENDING` status.
  - [x] Integrate the `stripe-java` SDK on the backend.
  - [x] Implement a service to create a Stripe Checkout Session based on the order details.
  - [x] Implement a webhook endpoint (`/api/stripe/events`) on the backend to receive payment status updates from Stripe.
  - [x] Update the order status in the database (`COMPLETED` or `FAILED`) based on the webhook event.

## **6. Critical Payment Flow Algorithm**

The end-to-end payment process must be implemented following these exact steps:

1.  User adds items to the cart and clicks the checkout button on the React frontend.
2.  Frontend sends a request to the backend API endpoint (e.g., `/api/orders/create`) with the cart contents.
3.  Backend creates a new `Order` entity in the PostgreSQL database with an initial status of `PENDING`.
4.  Backend uses the Stripe SDK to create a new Checkout Session, passing in order details like line items and total amount.
5.  The Stripe API returns a secure, unique checkout URL.
6.  The backend sends this checkout URL back to the frontend.
7.  Frontend automatically redirects the user's browser to the Stripe-hosted payment page.
8.  User enters payment details on Stripe's secure platform and completes the transaction.
9.  After payment, Stripe sends an asynchronous webhook event (e.g., `checkout.session.completed`) to the backend's webhook endpoint.
10. The backend verifies the webhook's signature, processes the event, and updates the corresponding order's status from `PENDING` to `COMPLETED`.
11. The user is redirected from the Stripe page back to a success/confirmation page on the React frontend.

## **7. Key Non-Functional Requirements**

### **Security**

  - **Password Hashing:** All user passwords must be hashed using BCrypt.
  - **Webhook Security:** The Stripe webhook endpoint **must** verify the signature of every incoming request to prevent fraudulent updates.
  - **CORS:** Configure Cross-Origin Resource Sharing (CORS) on the Spring Boot backend to allow requests from the React frontend.
  - **Environment Variables:** All sensitive information (database credentials, Stripe API keys, JWT secret) must be managed through environment variables, not hardcoded.
  - **HTTPS:** Enforce HTTPS in the production deployment.

### **Development Practices**

  - **Error Handling:** Implement robust error handling and logging on the backend.
  - **API Documentation:** Maintain clear documentation for all API endpoints.
  - **Testing:** Write unit tests for critical business logic, especially the payment and order processing services.
  - **Containerization:** The final application (frontend, backend, database) should be fully containerized using Docker and orchestrated with `docker-compose.yml`.

## **8. Getting Started**

### **Prerequisites**

- Docker
- Docker Compose
- Stripe Account with API keys

### **Configuration**

1.  **`docker-compose.yml`**:
    - `POSTGRES_PASSWORD`: Replace `password` with your desired database password.
    - `STRIPE_API_KEY`: Replace `your_stripe_api_key` with your Stripe secret key.
    - `STRIPE_WEBHOOK_SECRET`: Replace `your_stripe_webhook_secret` with your Stripe webhook signing secret.

### **Running the Application**

To run the application, you can use Docker Compose. Open a terminal in the root directory of the project and run the following command:

```bash
docker-compose up --build
```

The frontend will be available at `http://localhost:3000`.
The backend will be available at `http://localhost:8080`.

## **Available Scripts for Frontend**

In the `frontend` directory, you can run:

### `npm start`

Runs the app in the development mode.
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.

### `npm run build`

Builds the app for production to the `build` folder.
It correctly bundles React in production mode and optimizes the build for the best performance.

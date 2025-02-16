# Customer Transaction System

## Overview
This is a Spring Boot application that calculates reward points for customers based on their transactions. The reward points are calculated as follows:
- **1 point** for every dollar spent between **$50 and $100**.
- **2 points** for every dollar spent above **$100**.

For example:
- A purchase of $120 earns **90 points** (2 × 20 + 1 × 50).

The application calculates:
1. Monthly reward points per customer.
2. Total reward points for each customer over a 3-month period.

## API Endpoints
- `GET /api/customer-transactions/{customerId}/monthly`: Get monthly reward points for a customer.
- `GET /api/customer-transactions/{customerId}`: Get total reward points for a customer

## Technologies
- Spring Boot 3
- Java 23
- JUnit
- Mockito for testing
- Lombok for reducing boilerplate code.

## Installation
1. Clone this repository:   
   git clone https://github.com/geetanavhi0215/customer-transaction.git
   
2. Navigate to the project directory: 
   cd customer-transaction
   
3. Build the project:
   mvn clean install
   
4. Run the application
   mvn spring-boot:run
   
5. The application will be available at http://localhost:8080.
   
## Exception Handling
The application uses a global exception handler to manage exceptions and return meaningful error messages.

## Testing
Comprehensive test cases are included to validate the functionality of the application. Tests cover service methods, controller endpoints

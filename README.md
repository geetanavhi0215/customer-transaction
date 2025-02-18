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

** 1. Get Total Reward Points for a Customer**

**API: ** `GET /api/customer-transactions/{customerId}`

**Sample Request:** GET /api/customer-transactions/1

**Sample Response (Success):**

```json
{
    "customerId": 1,
    "totalPoints": 340,
    "monthlyRewardDetails": [
        {
            "month": "JANUARY",
            "rewardPoints": 90,
            "transactions": [
                {
                    "transactionId": 2,
                    "transactionDate": "2025-01-20",
                    "amountSpent": 110,
                    "rewardPoints": 70
                },
                {
                    "transactionId": 1,
                    "transactionDate": "2025-01-25",
                    "amountSpent": 70,
                    "rewardPoints": 20
                }
            ]
        },
        {
            "month": "FEBRUARY",
            "rewardPoints": 250,
            "transactions": [
                {
                    "transactionId": 3,
                    "transactionDate": "2025-02-01",
                    "amountSpent": 200,
                    "rewardPoints": 250
                }
            ]
        }
    ]
}
```


** Sample Response (Exception - CustomerId Not Found):**

```json
{
    "timestamp": "2025-02-17T22:50:57.9624956",
    "message": "CustomerId not found: 4",
    "details": "ERROR_VALIDATION"
}
```

** 2. Get Monthly Reward Points for a Customer**

**API: ** `GET /api/customer-transactions/{customerId}/monthly`

**Sample Request: **
**API: ** GET /api/customer-transactions/1/monthly

**Sample Response (Success):**

```json
{
  "JANUARY": 90,
  "FEBRUARY": 120
}
```

** 3. Get Reward Points for a Specific Month**

**API: ** `GET /api/customer-transactions/{customerId}/month/{month}`

**Sample Request: ** GET /api/customer-transactions/101/month/JANUARY

**Sample Response (Success):**

```json
{
    "month": "JANUARY",
    "totalPoints": 90,
    "transactions": [
        {
            "transactionId": 2,
            "transactionDate": "2025-01-20",
            "amountSpent": 110,
            "rewardPoints": 70
        },
        {
            "transactionId": 1,
            "transactionDate": "2025-01-25",
            "amountSpent": 70,
            "rewardPoints": 20
        }
    ]
}
```

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

# Loan Management API

A **Spring Boot based REST API** for managing loan applications, payments, and documents.  
This project simulates a simplified **Loan Management System** where users can apply for loans, track their loan status, pay EMIs, and upload documents.

The API is secured using **JWT Authentication** and documented with **Swagger (OpenAPI)**.

---
## Features
### Authentication
- User registration
- Login with JWT token
- Role-based access (USER / ADMIN)
  
### Loan Management
- Apply for loan
- View personal loans
- View loan details
- Admin can view all loans

### EMI Management
- Automatic EMI generation
- Track EMI status

### Payment Processing
- Pay EMI
- Payment tracking
- View all EMI of loans
- 
### Document Management
- Upload loan documents
- Associate documents with loan

### API Documentation
- Interactive API testing via Swagger Loan Management

Interactive API testing via Swagger
---

## Tech Stack

Backend Framework
- Spring Boot

Security
- Spring Security
- JSON Web Token (JWT)
- 
Database
- PostgreSQL
- Hibernate
- Spring Data JPA

API Documentation
- Swagger UI
- OpenAPI

Utilities
- Lombok
- Apache Maven

---

## System Architecture
```
Client (Frontend / Postman)
        │
        ▼
REST Controllers
        │
        ▼
Service Layer
        │
        ▼
Repository Layer
        │
        ▼
Database (PostgreSQL)

```

## Layers

Controller Layer
- Handles HTTP requests and responses.

Service Layer
- Contains business logic.

Repository Layer
- Handles database operations using JPA.

DTO Layer
- Transfers request and response data.

Security Layer
- Handles authentication and authorization using JWT.

## Project Structure
```
Loan-API
|
├──Configuration
|     ApiPrefixConfig
|     FilterConfig
|     SwaggerConfig
│
├── controller
│     AdminController
│     AuthController
│     DocumentController
│     EmailVerificationController
|     EMIController
|     Hello
|     LoanController
|     LoanPaymentController
|     PaymentController
|     UserController
│
├── dto
│     request models
│     response models
│
├── filter
│     JwtAuthenticationFilter
│
├──jwttoken
|     JwtUtils
│
├── entity/modul
│     User
│     Loan
│     EMISchedule
│     LoanPayment
│     Document
|     Email
│
├── repository
│     LoanRepository
│     UserRepository
|     DocumentRepository
|     EmailRepository
|     EMIRepository
|     LoanPaymentRepository
|
├── responseapi
|     APIResponse
│
├── service
|     AdminService
|     AuthService
│     DocumentService
|     EmailService
|     EMIService
|     UserDetailServiceImp
|     UserService
│     LoanService
│     LoanEmiPaymentService
|
├── status
|     DocumentStatus
|     DocumentType
|     EMIStatus
|     LoanStatus
|     PaymentMode
│
├── uitls
|     Common
|     EmailUtil
|     RazorPaymentVerificationUtil

```

## Authentication Flow
```
User Login
   │
   ▼
AuthController
   │
   ▼
JWT Token Generated
   │
   ▼
Client stores token
   │
   ▼
Client sends token in request header

Authorization: Bearer <token>
```

### API Modules
Authentication APIs
- POST /api/v1/auth/register
- POST /api/v1/auth/login

Loan APIs
- POST /api/v1/loans/{loan_id}/document
- POST /api/v1/loans/apply
- GET /api/v1/loans/my
- GET /api/v1/loans/{id}
- GET /api/v1/loans/get/all/loan

Document APIs
- POST /api/v1//loan/document/{documentId}/status
- GET /api/v1//loan/document/{loanId}/download/{documentId}
- GET /api/v1//loan/document/all

Payment APIs
- POST /api/v1//loan/emi/{loanId}/pay/{emiId}
- GET /api/v1//loan/emi/{id}/emi-schedule/get/all

Admin APIs
- GET /api/v1//admin/all/users

Email APIs
- GET /api/v1/email/verify

Payment APIs
- GET /api/v1/loan/emi/transaction/{loanId}

User APIs
- POST /api/v1/user/update
- GET /api/v1/user/get/user

Hello Test APIs
- GET /api/v1/hello/hello


### Example Loan Apply Request
- POST /api/v1/loans/apply


Request
```
{
  "amount": 50000,
  "interestRate": 10.5,
  "tenure": 12
}
```

Response
```
{
  "code": 0,
  "message": "string",
  "data": {
    "loan_id": 3,
    "amount": 50000,
    "interestRate": 12.5,
    "tenureMonths": 12,
    "status": "DOCUMENT_PENDING",
    "createdAt": "2026-03-16T12:43:56.614Z"
  }
}
```

## Running the Project
1 Clone the Repository
`git clone https://github.com/pankajtariyal/Loan-API.git`

2 Navigate to the Project
`cd Loan-API`

3 Configure Database
Update `application.properties`
```
spring.datasource.url=jdbc:postgresql://localhost:5432/loan_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

4 Build the Project
`mvn clean install`

5 Run the Application
`mvn spring-boot:run`

Server will start at
`http://localhost:8080/122`

### API Documentation
Swagger UI:
`http://localhost:8080/swagger-ui/index.html`

OpenAPI specification:
`http://localhost:8080/v3/api-docs`

Swagger allows:
- Viewing all APIs
- Testing endpoints
- Sending authenticated requests

Security

The API uses **JWT Authentication**.
Include the token in requests:
`Authorization: Bearer <JWT_TOKEN>`

Swagger UI also provides an **Authorize button** for authenticated requests.

## Future Improvements
- Loan approval workflow
- Credit score integration
- Notification service
- Payment gateway webhooks
- API rate limiting
- Docker deployment
- CI/CD pipeline

## Author

**Abhishek Tadiwal**

## GitHub
`https://github.com/pankajtariyal`

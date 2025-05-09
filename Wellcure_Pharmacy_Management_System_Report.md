# Wellcure Pharmacy Management System
## Project Report

---

## Overview

Wellcure is a comprehensive pharmacy management system designed to streamline prescription handling, medicine inventory management, and order processing. The application provides separate interfaces for users and administrators, allowing for efficient management of pharmacy operations.

---

## System Architecture

### Technology Stack
- **Language**: Java
- **UI Framework**: Java Swing
- **Database**: MySQL
- **Connectivity**: JDBC

### Database Structure
- **Users Table**: Stores user information (name, username, password, address, email)
- **Medicines Table**: Manages medicine inventory (name, type, price, stock)
- **Prescriptions Table**: Stores uploaded prescription images
- **Orders Table**: Tracks order status and links users with prescriptions
- **Order Items Table**: Manages the medicines included in each order

---

## Project Structure

- **src/ui**: User interface components
  - **src/ui/user**: User-facing screens
  - **src/ui/admin**: Admin-facing screens
- **src/database**: Database connection and utilities
- **src/Model**: Data models
- **src/controller**: Business logic and controllers

---

## User Features

### Authentication
- User login with username and password
- New user registration with name, username, password, email, and address

### Account Management
- View account details
- Profile information display

### Prescription Management
- Upload prescription images
- View order history
- Track order status

### Payment Processing
- Multiple payment options (COD, UPI)
- UPI payment with QR code generation
- Transaction ID verification

---

## Admin Features

### Authentication
- Secure admin login system

### Order Request Management
- View pending prescription orders
- View prescription images
- Confirm or reject orders
- Select medicines based on prescriptions
- Calculate total price based on quantity

### Stock Management
- View all medicines in inventory
- Add new medicines to inventory
- Update medicine details (name, type, price, stock)
- Remove medicines from inventory
- Automatic stock reduction upon order confirmation

### Payment Management
- Generate Excel reports for placed orders
- Payment verification for UPI transactions
- Update order status after payment verification

---

## Order Flow

1. **User uploads prescription** → Creates a draft order
2. **User checks out draft order** → Proceeds to payment selection
3. **User selects payment method**:
   - **COD**: Order status changes to "Placed"
   - **UPI**: User enters transaction ID, status changes to "Pending Verification"
4. **Admin reviews pending order** → Can view prescription and confirm/reject
5. **Admin selects medicines** → Adds to order and calculates total price
6. **Order status updated** → Changes to "Confirmed"
7. **Admin verifies payment** → For UPI payments, verifies transaction ID
8. **Order completed** → Status changes to "Placed" after payment verification

---

## UI Design

- Modern, clean aesthetic with a flat black and white theme
- Rounded buttons and UI elements
- Consistent styling across all pages
- Responsive layouts
- Image buttons for navigation
- App logo displayed in top left corner
- Indian Rupee symbol (₹) used for currency display

---

## Key Components

### StartWindow
- Entry point with options for User or Admin login
- Stylish exit button

### User Interface
- UserLoginPage: Authentication for regular users
- UserRegistrationPage: New account creation
- UserHomePage: Dashboard with medicine list and navigation
- AccountPage: User profile information
- UploadPrescriptionPage: Prescription image upload
- OrderPage: Order history and management
- PaymentSelectionPage: Payment method selection
- UPIPaymentPage: QR code generation and transaction ID entry

### Admin Interface
- AdminLoginPage: Authentication for administrators
- AdminPage: Dashboard with management options
- OrderRequestPage: View and manage prescription orders
- OrderConfirmationPage: Medicine selection and order confirmation
- StockManagementPage: Inventory management
- PaymentVerificationPage: Verify UPI payments
- ReportGenerator: Generate Excel reports for orders

---

## Security Features

- Password-protected user and admin accounts
- Separate interfaces for users and administrators
- Database transaction management for critical operations
- Input validation for all form fields

---

## Future Enhancements

- Online payment integration with more options
- Email notifications for order status updates
- Prescription validation system
- Advanced reporting and analytics
- Mobile application support
- User password reset functionality
- Enhanced security features

---

## Installation and Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher

### Database Setup
1. Create a MySQL database named `wellcure`
2. Run the SQL script located at `sql scipt/self database mini project.sql`

### Application Setup
1. Clone the repository
2. Configure database connection in `src/database/DBconnection.java`
3. Compile the Java files
4. Run the application with `java -cp bin Main`

---

## Conclusion

The Wellcure Pharmacy Management System provides a comprehensive solution for managing pharmacy operations, from prescription handling to inventory management and payment processing. Its user-friendly interface and robust functionality make it an effective tool for both pharmacy staff and customers.

---

© 2025 Wellcure Pharmacy Management System

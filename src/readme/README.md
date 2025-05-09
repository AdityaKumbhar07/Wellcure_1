# Wellcure Pharmacy Management System

## Overview
Wellcure is a comprehensive pharmacy management system designed to streamline the process of prescription handling, medicine inventory management, and order processing. The application provides separate interfaces for users and administrators, allowing for efficient management of pharmacy operations.

## Features

### User Features
- **User Registration and Authentication**: Secure login and registration system with email support
- **Account Management**: View and manage personal profile information
- **Prescription Upload**: Users can upload prescription images directly through the application
- **Order Management**: Track orders with different statuses (Draft, Pending, Confirmed, Placed, Rejected)
- **Payment Options**: Multiple payment methods including Cash on Delivery (COD) and UPI
- **UPI Payment**: QR code generation for UPI payments with transaction ID verification
- **Order History**: View past and current orders with their statuses and payment details

### Admin Features
- **Admin Authentication**: Secure login system for pharmacy administrators
- **Order Request Management**: View, confirm, or reject prescription orders
- **Medicine Selection**: Select appropriate medicines based on prescription images
- **Price Calculation**: Automatically calculate total price based on selected medicines and quantities
- **Stock Management**: Comprehensive medicine inventory management
  - View all medicines in stock
  - Add new medicines to inventory
  - Update medicine details (name, type, price, stock)
  - Remove medicines from inventory
- **Automatic Stock Updates**: Stock levels automatically reduced when orders are confirmed
- **Payment Verification**: Verify UPI payments by checking transaction IDs
- **Report Generation**: Generate Excel-format reports for placed orders

## System Architecture

### Database Structure
- **Users Table**: Stores user information (name, username, password, address, email)
- **Medicines Table**: Manages medicine inventory (name, type, price, stock)
- **Prescriptions Table**: Stores uploaded prescription images
- **Orders Table**: Tracks order status, payment method, transaction ID, and total price
- **Order Items Table**: Manages the medicines included in each order

### User Interface
The application features a clean, modern interface with:
- **Start Window**: Entry point with options for user or admin login
- **User Login/Registration**: Forms for authentication and new account creation
- **User Home Page**: Dashboard showing available medicines and navigation options
- **Admin Panel**: Dashboard for accessing administrative functions
- **Order Management Screens**: Interfaces for viewing and managing orders
- **Stock Management Screens**: Tools for inventory control
- **Payment Screens**: Interfaces for payment selection and processing

## Order Flow
1. **User uploads prescription** → Creates a draft order
2. **User checks out draft order** → Proceeds to payment selection screen
3. **User selects payment method**:
   - **COD**: Order status changes to "Placed" immediately
   - **UPI**: User scans QR code, enters transaction ID, status changes to "Pending Verification"
4. **Admin reviews pending order** → Can view prescription and confirm/reject
5. **Admin selects medicines** → Adds to order and calculates total price
6. **Order status updated** → Changes to "Confirmed" after medicine selection
7. **Admin verifies payment** → For UPI payments, verifies transaction ID
8. **Order completed** → Status changes to "Placed" after payment verification

## Technical Details

### Development Environment
- **Language**: Java
- **UI Framework**: Java Swing with modern flat design
- **Database**: MySQL
- **JDBC**: For database connectivity
- **Additional Libraries**: File handling for prescriptions, HTML generation for reports

### UI Design
- Modern, clean aesthetic with a flat black and white theme
- Rounded buttons and UI elements for a contemporary look
- Consistent styling across all pages
- Image buttons for navigation
- App logo displayed in the interface
- Indian Rupee symbol (₹) used for currency display

### Project Structure
- **src/ui**: User interface components
  - **src/ui/user**: User-facing screens
  - **src/ui/admin**: Admin-facing screens
  - **src/ui/util**: UI utilities and styling
- **src/database**: Database connection and utilities
- **src/Model**: Data models
- **src/controller**: Business logic and controllers
- **src/images**: UI images and icons

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

### Default Credentials
- **Admin Login**:
  - Username: admin
  - Password: admin123

## Future Enhancements
- Online payment integration with more options
- Email notifications for order status updates
- Prescription validation system
- Advanced reporting and analytics
- Mobile application support
- User password reset functionality
- Enhanced security features

## Contributors
- Aditya Kumbhar

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

© 2025 Wellcure Pharmacy Management System. All rights reserved.

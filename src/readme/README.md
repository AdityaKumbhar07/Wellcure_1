# Wellcure Pharmacy Management System

## Overview
Wellcure is a comprehensive pharmacy management system designed to streamline the process of prescription handling, medicine inventory management, and order processing. The application provides separate interfaces for users and administrators, allowing for efficient management of the pharmacy operations.

## Features

### User Features
- **User Registration and Authentication**: Secure login and registration system for customers
- **Prescription Upload**: Users can upload prescription images directly through the application
- **Order Management**: Track orders with different statuses (Draft, Pending, Confirmed, Rejected)
- **Checkout Process**: Simple payment process with Cash on Delivery (COD) option
- **Order History**: View past and current orders with their statuses

### Admin Features
- **Admin Authentication**: Secure login system for pharmacy administrators
- **Order Request Management**: View, confirm, or reject prescription orders
- **Stock Management**: Comprehensive medicine inventory management
  - View all medicines in stock
  - Add new medicines to inventory
  - Update medicine details (name, type, price, stock)
  - Remove medicines from inventory
- **Payment Management**: Track and manage payment information

## System Architecture

### Database Structure
- **Users Table**: Stores user information (name, username, password, address)
- **Medicines Table**: Manages medicine inventory (name, type, price, stock)
- **Prescriptions Table**: Stores uploaded prescription images
- **Orders Table**: Tracks order status and links users with prescriptions
- **Order Items Table**: Manages the medicines included in each order

### User Interface
The application features a clean, intuitive interface with:
- **Start Window**: Entry point with options for user or admin login
- **User Login/Registration**: Forms for authentication and new account creation
- **Admin Panel**: Dashboard for accessing administrative functions
- **Order Management Screens**: Interfaces for viewing and managing orders
- **Stock Management Screens**: Tools for inventory control

## Order Flow
1. **User uploads prescription** → Creates a draft order
2. **User checks out draft order** → Proceeds to payment screen
3. **User confirms payment (COD)** → Order status changes to "Pending"
4. **Admin reviews pending order** → Can view prescription and confirm/reject
5. **Order status updated** → Changes to "Confirmed" or "Rejected"

## Technical Details

### Development Environment
- **Language**: Java
- **UI Framework**: Java Swing
- **Database**: MySQL
- **JDBC**: For database connectivity

### Project Structure
- **src/ui**: User interface components
  - **src/ui/user**: User-facing screens
  - **src/ui/admin**: Admin-facing screens
- **src/database**: Database connection and utilities
- **src/Model**: Data models
- **src/controller**: Business logic and controllers

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
- Online payment integration
- Email notifications for order status updates
- Prescription validation system
- Advanced reporting and analytics
- Mobile application support

## Contributors
- Aditya Kumbhar

## License
This project is licensed under the [License Name] - see the LICENSE file for details.

---

© 2025 Wellcure Pharmacy Management System. All rights reserved.

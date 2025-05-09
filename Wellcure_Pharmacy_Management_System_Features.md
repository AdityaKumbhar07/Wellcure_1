# Wellcure Pharmacy Management System
## Key Features and Components

---

## 1. User Management

### User Registration
- New users can create accounts with:
  - Name
  - Username (unique)
  - Password
  - Email
  - Address
- Input validation ensures data integrity

### User Authentication
- Secure login system for regular users
- Username and password verification
- Session management

### Account Management
- Users can view their account details
- Profile information display includes:
  - Name
  - Username
  - Email
  - Address

---

## 2. Admin Management

### Admin Authentication
- Separate secure login system for administrators
- Default credentials provided for initial access

### Admin Dashboard
- Central hub for all administrative functions
- Clean, intuitive interface with four main options:
  - Order Requests
  - Manage Stock
  - Manage Payment
  - Payment Verification
- Modern UI with app logo and navigation controls

---

## 3. Prescription Management

### Prescription Upload
- Users can upload prescription images
- File selection dialog for easy image selection
- Automatic creation of draft orders upon upload

### Prescription Viewing
- Admins can view uploaded prescriptions
- High-quality image display for accurate medicine selection

---

## 4. Order Processing

### Order Creation
- Automatic draft order creation upon prescription upload
- Orders linked to user accounts and prescriptions

### Order Status Tracking
- Multiple status stages:
  - Draft: Initial state after prescription upload
  - Pending: After user checkout
  - Confirmed: After admin medicine selection
  - Placed: After payment verification
  - Rejected: If admin rejects the order

### Order History
- Users can view their complete order history
- Details include status, prescription, and payment information

---

## 5. Medicine Selection

### Medicine Review
- Admins can select appropriate medicines based on prescriptions
- Add multiple medicines to a single order

### Quantity Management
- Specify quantity for each medicine
- Automatic price calculation based on quantity and unit price

### Order Confirmation
- Calculate total order price
- Update order status to "Confirmed"
- Automatic stock reduction upon confirmation

---

## 6. Inventory Management

### Stock Viewing
- Complete list of all medicines in inventory
- Details include name, type, price, and current stock

### Stock Addition
- Add new medicines to inventory
- Specify name, type, price, and initial stock

### Stock Updates
- Modify existing medicine details
- Update prices, types, or stock levels

### Stock Removal
- Remove obsolete or discontinued medicines
- Confirmation dialog to prevent accidental deletion

### Automatic Stock Tracking
- Stock levels automatically reduced when orders are confirmed
- Prevents overselling of medicines

---

## 7. Payment Processing

### Payment Method Selection
- Users can choose between:
  - Cash on Delivery (COD)
  - UPI Payment

### COD Processing
- Simple confirmation process
- Order status updated to "Placed" immediately

### UPI Payment
- Dynamic QR code generation based on order amount
- UPI ID and merchant details included
- Transaction ID entry for verification

### Payment Verification
- Admins can verify UPI payments
- Transaction ID validation
- Order status updated after verification

---

## 8. Reporting

### Excel Report Generation
- Generate reports for placed orders
- HTML-based Excel format for compatibility
- Automatic file naming with timestamps

### Report Content
- Order ID
- Customer Name
- Total Amount
- Payment Method
- Date

### Report Access
- Reports saved to local directory
- Automatic opening of generated reports
- Easy sharing and printing

---

## 9. User Interface Design

### Modern Aesthetic
- Clean, flat design throughout the application
- Black and white color theme
- Rounded corners for buttons and UI elements

### Consistent Styling
- Uniform look and feel across all pages
- Standardized button sizes and placements
- Consistent spacing and alignment

### Navigation
- Intuitive navigation between screens
- Back buttons for easy return to previous screens
- Image buttons for common actions

### Responsive Layout
- Properly sized windows for different content
- Scrollable areas for large data sets
- Appropriate spacing between elements

---

## 10. Security Features

### Authentication Security
- Password-protected access for all users
- Role-based access control (user vs. admin)

### Input Validation
- Form field validation for all user inputs
- Error messages for invalid inputs
- Prevention of SQL injection

### Transaction Management
- Database transactions for critical operations
- Rollback capability for failed operations
- Data integrity protection

---

## 11. Technical Implementation

### Database Connectivity
- JDBC for MySQL database connection
- Connection pooling for efficiency
- Prepared statements for secure queries

### MVC Architecture
- Model: Data objects and database interactions
- View: User interface components
- Controller: Business logic and application flow

### Error Handling
- Comprehensive exception handling
- User-friendly error messages
- Logging for debugging purposes

### Code Organization
- Logical package structure
- Separation of concerns
- Reusable components and utilities

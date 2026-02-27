# E-Commerce Application

A Java-based desktop E-Commerce application with a relational database backend. The application features user authentication, role-based access control, product browsing, cart management, and order processing.

## Features

This system provides tailored dashboards and functionalities for three main user roles:

### Customers
- Create an account and manage profile/addresses
- Browse the product catalog with categories
- Add items to a shopping cart and place orders
- View order history and order statuses
- Leave reviews and ratings for purchased products
- View personal statistics and recent activities

### Sellers
- Manage personalized catalogs and products
- Track inventory and set pricing
- View and manage customer orders for their products
- Track sales statistics and product performance

### Administrators
- Manage platform users (Customers, Sellers, and other Admins)
- Oversee the product categories and global settings
- Track and update shipment statuses for all orders
- View platform-wide statistics, sales, and analytics

## Technologies Used

- **Language**: Java
- **GUI Framework**: Java Swing / AWT
- **Database Backend**: MySQL
- **Database Access**: JDBC

## How to Run

1. **Set Up the Database**:
   - Create a new relational database (e.g., MySQL).
   - Execute the `DDL.sql` script to create the required tables.
   - Execute the `DML.sql` script to populate the tables with initial dummy data.
2. **Configure Database Connection**:
   - Ensure you have the proper JDBC driver inside the `lib/` directory or added to your project's build path/classpath.
   - Update `DatabaseManager.java` (or any configuration file if present) with your database's URL, username, and password.
3. **Compile and Run**:
   - Compile the Java source files in the `src/` directory.
   - Run the `Main` class to start the application.
   - Login using one of the configured accounts from the `DML.sql` or create a new user via the Registration window.

## References

This project was developed collaboratively with my teammate. You can find his GitHub profile here: [Arda Ayas](https://github.com/ArdaAyas)

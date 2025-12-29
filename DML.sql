-- Insert Users
INSERT INTO USERS (Username, Email, PasswordHash, UserRole, FirstName, LastName)
VALUES
('admin.arda', 'arda.ayas@ozu.edu.tr', 'hashed_pw_1', 'Administrator', 'Arda', 'Ayas'),
('seller.bora', 'bora.kisalar@ozu.edu.tr', 'hashed_pw_2', 'Seller', 'Bora', 'Kisalar'),
('customer.ali', 'ali.yilmaz@gmail.com', 'hashed_pw_3', 'Customer', 'Ali', 'Yilmaz'),
('customer.ayse', 'ayse.demir@gmail.com', 'hashed_pw_4', 'Customer', 'Ayse', 'Demir');


INSERT INTO ADDRESSES (UserID, AddressType, StreetAddress, City, District, Neighborhood, ZipCode, Country)
VALUES
(3, 'Shipping', 'Ataturk Cad. No:5', 'Istanbul', 'Kadikoy', 'Moda', '34710', 'Turkiye'),
(3, 'Billing', 'Ataturk Cad. No:5', 'Istanbul', 'Kadikoy', 'Moda', '34710', 'Turkiye');


INSERT INTO ADDRESSES (UserID, AddressType, StreetAddress, City, District, Neighborhood, ZipCode, Country)
VALUES
(4, 'Shipping', 'Papatya Sok. No:12', 'Ankara', 'Cankaya', 'Bahcelievler', '06490', 'Turkiye');

INSERT INTO CATEGORIES (CategoryName, Description)
VALUES
('Electronics', 'Modern electronic devices and gadgets.'),
('Books', 'Academic and fictional literature.'),
('Home & Garden', 'Furniture and home decoration items.');

INSERT INTO CATALOGS (SellerID, CatalogName)
VALUES
(2, 'Bora Tech & Books Catalog');

INSERT INTO PRODUCTS (CatalogID, CategoryID, ProductName, Description, Price, StockQuantity)
VALUES
(1, 1, 'Gaming Laptop Pro', 'High-performance gaming machine.', 1500.00, 10),
(1, 1, 'Wireless Mouse', 'Ergonomic 2.4GHz mouse.', 50.00, 100),
(1, 2, 'Database Systems 101', 'Comprehensive guide to SQL.', 80.00, 50);

INSERT INTO ORDERS (CustomerID, SellerID, ShippingAddressID, TotalAmount, OrderStatus)
VALUES
(3, 2, 1, 1550.00, 'shipped');

INSERT INTO ORDER_ITEMS (OrderID, ProductID, Quantity, PurchasePrice)
VALUES
(1, 1, 1, 1500.00),
(1, 2, 1, 50.00);

INSERT INTO PAYMENTS (OrderID, Amount, PaymentMethod, PaymentStatus)
VALUES
(1, 1550.00, 'credit card', 'completed');

INSERT INTO SHIPMENTS (OrderID, TrackingNumber, ShipmentStatus)
VALUES
(1, 'TR123456789', 'shipped');

INSERT INTO REVIEWS (OrderItemID, CustomerID, Rating, Comment)
VALUES
(1, 3, 5, 'Best laptop I have ever used. Very fast!');

COMMIT;


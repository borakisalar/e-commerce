INSERT INTO USERS (Username, Email, PasswordHash, UserRole, FirstName, LastName)
VALUES
('bora.kisalar', 'bora.kisalar@ozu.edu.tr', 'my_hashed_password', 'Seller', 'Bora', 'Kisalar'),
('arda.ayas', 'arda.ayas@ozu.edu.tr', 'another_hashed_password', 'Administrator', 'Arda', 'Ayas'),
('ali.yilmaz', 'ali.yilmaz@gmail.com', 'different_hashed_password', 'Customer', 'Ali', 'Yilmaz');

INSERT INTO ADDRESSES (UserID, AddressType, StreetAddress, City, District, Neighborhood, ZipCode, Country)
VALUES
(2, 'Shipping', 'Sarikanarya sok.', 'Istanbul', 'Kadikoy', 'Kozyatagi mah.', '34000', 'Turkiye'),
(2, 'Billing', 'Sarikanarya sok.', 'Istanbul', 'Kadikoy', 'Kozyatagi mah.', '34000', 'Turkiye'),
(3, 'Shipping', 'Vezir sok.', 'Istanbul', 'Fatih', 'Kocamustafapasa mah.', '34000', 'Turkiye');

INSERT INTO CATEGORIES (CategoryName, Description)
VALUES
('Books', 'All kinds of book easily found here.'),
('Games', 'Every type of game found here.'),
('Electronics', 'Almost every electronic devices found here.');

INSERT INTO CATALOGS (SellerID, CatalogName)
VALUES
(1, 'Tech Seller Catalog');

INSERT INTO PRODUCTS (CatalogID, CategoryID, ProductName, Description, Price, StockQuantity)
VALUES
(1, 1, 'Gaming Laptop', 'A high-performance laptop for gamers.', 1299.99, 50),
(1, 1, 'Wireless Mouse with bluetooth', 'Ergonomic wireless mouse with 5 buttons and bluetooth connectivity.', 49.99, 200),
(1, 2, 'The MySQL Guide', 'Advanced MySQL book for students.', 79.50, 100);

INSERT INTO ORDERS (CustomerID, SellerID, ShippingAddressID, TotalAmount, OrderStatus)
VALUES
(3, 1, 1, 1349.98, 'shipped');

INSERT INTO ORDER_ITEMS (OrderID, ProductID, Quantity, PurchasePrice)
VALUES
(1, 1, 1, 1199.99),
(1, 2, 1, 59.99);

INSERT INTO PAYMENTS (OrderID, Amount, PaymentMethod, PaymentStatus)
VALUES
(1, 1339.98, 'credit card', 'completed');

INSERT INTO SHIPMENTS (OrderID, TrackingNumber, ShipmentStatus)
VALUES
(1, '1Z999AA10123454729', 'shipped');

INSERT INTO REVIEWS (OrderItemID, CustomerID, Rating, Comment)
VALUES
(1, 3, 5, 'Absolutely fantastic laptop! Fast and reliable.');

COMMIT;

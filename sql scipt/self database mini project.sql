-- create database
create database wellcure;
use wellcure;

-- creat user table
create table users(
	user_id INT auto_increment key,
    name varchar(100),
    username varchar(20) unique,
    password varchar(20),
    address varchar(100)
);

-- create medicine table
create table medicines(
	medicine_id int auto_increment primary key,
    medicine_name varchar(40),
    type varchar(20),
    price double,
    stock int
);

CREATE TABLE prescriptions (
    prescriptions_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    image_path VARCHAR(300),
    status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- create order table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    prescription_id INT,
    order_status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(prescription_id)
);


-- Insert sample data
INSERT INTO users (user_id, username, password) VALUES ('1', 'admin', '1234');
INSERT INTO users (username, password, name, address) VALUES ('user1', 'password1', 'John Doe', '123 Main St');

INSERT INTO medicines (medicine_id,medicine_name, type, price, stock)
VALUES (1,'Paracetamol', 'tablet', 20.5, 100);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES (2,'Azytramycine', 'tablet', 40, 150);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Ibuprofen', 'tablet', 25.75, 80);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Amoxicillin', 'capsule', 35.25, 120);

-- Insert sample prescriptions
INSERT INTO prescriptions (user_id, image_path, status)
VALUES (2, 'prescriptions/prescription1.jpg', 'Submitted');

INSERT INTO prescriptions (user_id, image_path, status)
VALUES (2, 'prescriptions/prescription2.jpg', 'Submitted');

-- Insert sample orders
INSERT INTO orders (user_id, prescription_id, order_status)
VALUES (2, 1, 'Draft');

INSERT INTO orders (user_id, prescription_id, order_status)
VALUES (2, 2, 'Pending');

-- Insert sample order items
INSERT INTO order_items (order_id, medicine_id, medicine_price, quantity)
VALUES (1, 1, 20.5, 2);

INSERT INTO order_items (order_id, medicine_id, medicine_price, quantity)
VALUES (1, 3, 25.75, 1);

INSERT INTO order_items (order_id, medicine_id, medicine_price, quantity)
VALUES (2, 2, 40, 3);

INSERT INTO order_items (order_id, medicine_id, medicine_price, quantity)
VALUES (2, 4, 35.25, 2);

-- Cleanup commands (commented out for safety)
-- drop table medicine;
-- delete from users where username = 'a';
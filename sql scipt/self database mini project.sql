-- create database
create database wellcure;
use wellcure;

- creat user table
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
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
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

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Paracetamol', 'tablet', 20.5, 100);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Azytramycine', 'tablet', 40, 150);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Ibuprofen', 'tablet', 25.75, 80);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Amoxicillin', 'capsule', 35.25, 120);

-- sample medicine
INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Paracetamol', 'tablet', 20.5, 100);

INSERT INTO medicines (medicine_name, type, price, stock)
VALUES ('Azytramycine', 'tablet', 40, 150);

-- Cleanup commands (commented out for safety)
-- drop table medicine;
-- delete from users where username = 'a';
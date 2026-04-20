-- Drop tables if exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS pet_food_relationship;
DROP TABLE IF EXISTS pet_supplier_relationship;
DROP TABLE IF EXISTS employee_pet_relationship;
DROP TABLE IF EXISTS pet_vaccination_relationship;
DROP TABLE IF EXISTS pet_grooming_relationship;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS pet_food;
DROP TABLE IF EXISTS vaccinations;
DROP TABLE IF EXISTS grooming_services;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS pet_categories;
DROP TABLE IF EXISTS addresses;

-- Main tables
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20)
);

CREATE TABLE pet_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE pets (
    pet_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    breed VARCHAR(100),
    age INT,
    price DOUBLE,
    description TEXT,
    image_url VARCHAR(500),
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES pet_categories(category_id) ON DELETE SET NULL
);

CREATE TABLE grooming_services (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    price DOUBLE,
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE vaccinations (
    vaccination_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    price DOUBLE,
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    position VARCHAR(100),
    phone_number VARCHAR(20)
);

CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
    contact_person VARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE pet_food (
    food_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
    brand VARCHAR(100),
    type VARCHAR(50),
    quantity INT,
    price DOUBLE
);

CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150),
    phone_number VARCHAR(20),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    pet_id INT,
    transaction_date DATE,
    amount DOUBLE,
    transaction_status VARCHAR(20)
);

-- Join tables
CREATE TABLE pet_grooming_relationship (
    pet_id INT,
    service_id INT,
    PRIMARY KEY (pet_id, service_id),
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES grooming_services(service_id) ON DELETE CASCADE
);

CREATE TABLE pet_vaccination_relationship (
    pet_id INT,
    vaccination_id INT,
    PRIMARY KEY (pet_id, vaccination_id),
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    FOREIGN KEY (vaccination_id) REFERENCES vaccinations(vaccination_id) ON DELETE CASCADE
);

CREATE TABLE employee_pet_relationship (
    pet_id INT,
    employee_id INT,
    PRIMARY KEY (pet_id, employee_id),
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

CREATE TABLE pet_food_relationship (
    pet_id INT,
    food_id INT,
    PRIMARY KEY (pet_id, food_id),
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES pet_food(food_id) ON DELETE CASCADE
);

CREATE TABLE pet_supplier_relationship (
    pet_id INT,
    supplier_id INT,
    PRIMARY KEY (pet_id, supplier_id),
    FOREIGN KEY (pet_id) REFERENCES pets(pet_id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE CASCADE
);

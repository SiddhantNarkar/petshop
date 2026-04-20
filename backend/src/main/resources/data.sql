-- Insert sample data into tables

-- Pet Categories
INSERT INTO pet_categories (name) VALUES 
('Dogs'),
('Cats'),
('Birds'),
('Fish'),
('Rabbits');

-- Addresses
INSERT INTO addresses (street, city, state, zip_code) VALUES 
('123 Main St', 'Springfield', 'IL', '62701'),
('456 Oak Ave', 'Chicago', 'IL', '60601'),
('789 Pine Rd', 'Naperville', 'IL', '60540'),
('321 Elm Blvd', 'Aurora', 'IL', '60502');

-- Customers
INSERT INTO customers (first_name, last_name, email, phone_number, address_id) VALUES 
('John', 'Doe', 'john.doe@example.com', '555-1234', 1),
('Jane', 'Smith', 'jane.smith@example.com', '555-5678', 2),
('Robert', 'Johnson', 'robert.j@example.com', '555-9012', 3),
('Emily', 'Williams', 'emily.w@example.com', '555-3456', 4);

-- Suppliers
INSERT INTO suppliers (name, contact_person, phone_number, email) VALUES 
('Pet Supplies Co', 'Mike Johnson', '555-0001', 'mike@petsupplies.com'),
('Animal Foods Ltd', 'Sarah Lee', '555-0002', 'sarah@animalfoods.com'),
('Premium Pet Products', 'Tom Brown', '555-0003', 'tom@premiumpet.com');

-- Employees
INSERT INTO employees (first_name, last_name, position, phone_number) VALUES 
('Alex', 'Brown', 'Veterinarian', '555-1001'),
('Emma', 'Wilson', 'Groomer', '555-1002'),
('Michael', 'Davis', 'Pet Trainer', '555-1003'),
('Sophia', 'Martinez', 'Sales Associate', '555-1004');

-- Pet Food
INSERT INTO pet_food (name, brand, type, quantity, price) VALUES 
('Premium Dog Food', 'PetMax', 'Dry', 50, 29.99),
('Cat Treats', 'Whiskers', 'Snack', 100, 12.99),
('Puppy Formula', 'NutriPaws', 'Dry', 30, 45.99),
('Adult Cat Food', 'Purrizen', 'Dry', 45, 34.99),
('Bird Seeds Mix', 'FeatherFriends', 'Seeds', 80, 15.99),
('Fish Flakes', 'AquaLife', 'Dry', 60, 8.99),
('Rabbit Pellets', 'HoppyLife', 'Pellets', 40, 18.99);

-- Grooming Services
INSERT INTO grooming_services (name, description, price, available) VALUES 
('Full Grooming', 'Complete bath, trim, and nail clip', 45.00, true),
('Bath Only', 'Full bath with blow dry', 25.00, true),
('Nail Trimming', 'Nail clip and filing', 15.00, true),
('Teeth Cleaning', 'Dental cleaning service', 35.00, true);

-- Vaccinations
INSERT INTO vaccinations (name, description, price, available) VALUES 
('Rabies', 'Rabies vaccination', 35.00, true),
('DHPP', 'Distemper, Hepatitis, Parvo, Parainfluenza', 50.00, true),
('Bordetella', 'Kennel cough vaccine', 25.00, true),
('FVRCP', 'Feline distemper vaccine', 30.00, true);

-- Pets
INSERT INTO pets (name, breed, age, price, description, image_url, category_id) VALUES 
('Buddy', 'Golden Retriever', 3, 499.99, 'Friendly and playful', 'https://images.unsplash.com/photo-1552053831-71594a27632d?w=300', 1),
('Luna', 'Siamese Cat', 2, 299.99, 'Calm and elegant', 'https://images.unsplash.com/photo-1513245543132-31f507417b26?w=300', 2),
('Charlie', 'Labrador', 4, 449.99, 'Energetic and loyal', 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=300', 1),
('Milo', 'Persian Cat', 1, 349.99, 'Fluffy and affectionate', 'https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=300', 2),
('Tweety', 'Canary', 2, 49.99, 'Beautiful singing bird', 'https://images.unsplash.com/photo-1552728089-57bdde30beb3?w=300', 3),
('Nemo', 'Clownfish', 1, 19.99, 'Colorful and active', 'https://images.unsplash.com/photo-1535591273668-578e31182c4f?w=300', 4),
('Thumper', 'Holland Lop', 1, 89.99, 'Cute and cuddly', 'https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308?w=300', 5),
('Rocky', 'German Shepherd', 5, 599.99, 'Intelligent and protective', 'https://images.unsplash.com/photo-1589941013453-ec89f33b5e95?w=300', 1);

-- Transactions
INSERT INTO transactions (customer_id, pet_id, transaction_date, amount, transaction_status) VALUES 
(1, 1, '2026-04-15', 499.99, 'Success'),
(2, 2, '2026-04-16', 299.99, 'Success'),
(3, 3, '2026-04-17', 449.99, 'Success'),
(1, 4, '2026-04-10', 349.99, 'Success'),
(4, 8, '2026-04-14', 599.99, 'Success');

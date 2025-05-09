-- SQL Script to populate the medicines table with realistic data
-- For Wellcure Pharmacy Management System

-- First, make sure we're using the correct database
USE wellcure;

-- Clear existing data (optional - remove this if you want to keep existing entries)
-- TRUNCATE TABLE medicines;

-- Insert medicine data
INSERT INTO medicines (medicine_name, type, price, stock) VALUES
-- Antibiotics
('Amoxicillin 500mg', 'Antibiotic', 120.00, 150),
('Azithromycin 250mg', 'Antibiotic', 180.50, 100),
('Ciprofloxacin 500mg', 'Antibiotic', 145.75, 80),
('Doxycycline 100mg', 'Antibiotic', 95.25, 120),
('Cephalexin 250mg', 'Antibiotic', 110.50, 90),

-- Pain Relievers
('Paracetamol 500mg', 'Pain Reliever', 25.00, 300),
('Ibuprofen 400mg', 'Pain Reliever', 35.50, 250),
('Diclofenac 50mg', 'Pain Reliever', 45.75, 180),
('Aspirin 300mg', 'Pain Reliever', 20.25, 200),
('Naproxen 250mg', 'Pain Reliever', 55.00, 150),

-- Antacids
('Pantoprazole 40mg', 'Antacid', 85.50, 120),
('Ranitidine 150mg', 'Antacid', 65.25, 100),
('Omeprazole 20mg', 'Antacid', 75.00, 130),
('Famotidine 20mg', 'Antacid', 60.75, 90),
('Esomeprazole 40mg', 'Antacid', 95.25, 80),

-- Antihistamines
('Cetirizine 10mg', 'Antihistamine', 45.50, 200),
('Loratadine 10mg', 'Antihistamine', 55.25, 180),
('Fexofenadine 120mg', 'Antihistamine', 75.00, 150),
('Chlorpheniramine 4mg', 'Antihistamine', 35.75, 220),
('Diphenhydramine 25mg', 'Antihistamine', 40.25, 190),

-- Antidiabetics
('Metformin 500mg', 'Antidiabetic', 65.50, 100),
('Glimepiride 2mg', 'Antidiabetic', 85.25, 80),
('Sitagliptin 50mg', 'Antidiabetic', 195.00, 60),
('Gliclazide 80mg', 'Antidiabetic', 75.75, 90),
('Vildagliptin 50mg', 'Antidiabetic', 210.25, 50),

-- Antihypertensives
('Amlodipine 5mg', 'Antihypertensive', 55.50, 120),
('Losartan 50mg', 'Antihypertensive', 75.25, 100),
('Enalapril 5mg', 'Antihypertensive', 65.00, 110),
('Telmisartan 40mg', 'Antihypertensive', 95.75, 90),
('Ramipril 2.5mg', 'Antihypertensive', 85.25, 80),

-- Vitamins & Supplements
('Vitamin C 500mg', 'Supplement', 45.50, 250),
('Vitamin D3 1000IU', 'Supplement', 65.25, 200),
('Calcium 500mg', 'Supplement', 55.00, 180),
('Multivitamin Tablet', 'Supplement', 75.75, 220),
('Iron 50mg', 'Supplement', 40.25, 150),

-- Antidepressants
('Fluoxetine 20mg', 'Antidepressant', 95.50, 80),
('Sertraline 50mg', 'Antidepressant', 105.25, 70),
('Escitalopram 10mg', 'Antidepressant', 115.00, 60),
('Amitriptyline 25mg', 'Antidepressant', 85.75, 90),
('Venlafaxine 75mg', 'Antidepressant', 125.25, 50),

-- Antivirals
('Acyclovir 400mg', 'Antiviral', 135.50, 70),
('Oseltamivir 75mg', 'Antiviral', 225.25, 50),
('Valacyclovir 500mg', 'Antiviral', 195.00, 60),
('Ribavirin 200mg', 'Antiviral', 175.75, 40),
('Famciclovir 250mg', 'Antiviral', 185.25, 30),

-- Topical Medications
('Betamethasone Cream 0.1%', 'Topical', 65.50, 100),
('Clotrimazole Cream 1%', 'Topical', 55.25, 120),
('Mupirocin Ointment 2%', 'Topical', 75.00, 80),
('Salicylic Acid Gel 2%', 'Topical', 45.75, 90),
('Calamine Lotion 100ml', 'Topical', 35.25, 110);

-- Verify the data was inserted correctly
-- SELECT * FROM medicines ORDER BY type, medicine_name;

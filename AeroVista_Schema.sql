CREATE DATABASE IF NOT EXISTS AeroVista_project;
USE AeroVista_project;

-- Table 1: Products
CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10,2),
    launch_date DATE
);

-- Table 2: Sales
CREATE TABLE Sales (
    sale_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    quantity INT,
    revenue DECIMAL(12,2),
    sale_date DATE,
    region VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- Table 3: Investment
CREATE TABLE Investment (
    inv_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    month VARCHAR(20),
    investment_type VARCHAR(50),
    amount DECIMAL(12,2),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- Table 4: Survey
CREATE TABLE Survey (
    survey_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    feature_name VARCHAR(100),
    rating INT CHECK(rating BETWEEN 1 AND 5),
    feedback_text TEXT,
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);

-- Insert Sample Products
INSERT INTO Products (name, category, price, launch_date) VALUES
('Product A', 'Electronics', 15000.00, '2024-01-15'),
('Product B', 'Clothing', 2000.00, '2025-02-10'),
('Product C', 'Food', 500.00, '2025-03-05');

-- Insert Sample Sales
INSERT INTO Sales (product_id, quantity, revenue, sale_date, region) VALUES
(1, 15, 205000.00, '2025-04-01', 'north'),
(2, 55, 500000.00, '2025-05-05', 'south'),
(3, 80, 100000.00, '2025-05-10', 'east');

-- Insert Sample Investments
INSERT INTO Investment (product_id, month, investment_type, amount) VALUES
(1, 'April', 'Ads', 50000.00),
(2, 'April', 'Promotions', 20000.00),
(3, 'April', 'R&D', 15000.00);

-- Insert Sample Surveys
INSERT INTO Survey (product_id, feature_name, rating, feedback_text) VALUES
(1, 'Battery Life', 4, 'Good but can improve'),
(2, 'Cloth Quality', 5, 'Excellent fabric'),
(3, 'Taste', 3, 'Average taste, needs improvement');


DROP VIEW IF EXISTS feature_rating;
-- Create new view
CREATE VIEW feature_rating AS
SELECT
    p.product_id,
    p.name AS product_name,
    LOWER(s.feature_name) AS feature_name,   -- normalize feature names
    ROUND(AVG(s.rating), 2) AS avg_rating,
    COUNT(*) AS responses,
    ROW_NUMBER() OVER (ORDER BY p.product_id, LOWER(s.feature_name)) AS feature_rating_id
FROM Survey s
JOIN Products p 
    ON s.product_id = p.product_id
GROUP BY 
    p.product_id, 
    p.name, 
    LOWER(s.feature_name);
    
    
    DROP VIEW IF EXISTS product_roi;
    CREATE VIEW product_roi AS
SELECT 
  p.product_id,
  p.name AS product_name,
  COALESCE(SUM(s.revenue),0) AS total_revenue,
  COALESCE(SUM(i.amount),0) AS total_investment,
  (COALESCE(SUM(s.revenue),0) - COALESCE(SUM(i.amount),0)) AS profit,
  CASE 
    WHEN COALESCE(SUM(i.amount),0) = 0 THEN NULL
    ELSE ROUND(((COALESCE(SUM(s.revenue),0) - COALESCE(SUM(i.amount),0)) / COALESCE(SUM(i.amount),0)) * 100,2)
  END AS roi_percent
FROM Products p
LEFT JOIN Sales s ON s.product_id = p.product_id
LEFT JOIN Investment i ON i.product_id = p.product_id
GROUP BY p.product_id, p.name;



DROP VIEW IF EXISTS region_sales;
CREATE VIEW region_sales AS
SELECT 
  CONCAT(p.product_id, '-', s.region) AS region_id,
  p.product_id,
  p.name AS product_name,
  s.region,
  SUM(s.quantity) AS total_quantity,
  SUM(s.revenue) AS total_revenue
FROM Sales s
JOIN Products p ON s.product_id = p.product_id
GROUP BY 
  p.product_id,
  p.name,
  s.region,
  CONCAT(p.product_id, '-', s.region);
  
  
   DROP VIEW IF EXISTS monthly_sales;
CREATE VIEW monthly_sales AS
SELECT 
  CONCAT(p.product_id, '-', DATE_FORMAT(s.sale_date, '%Y-%m')) AS monthly_id,
  p.product_id,
  p.name AS product_name,
  DATE_FORMAT(s.sale_date, '%Y-%m') AS yearmonth,
  SUM(s.quantity) AS total_quantity,
  SUM(s.revenue) AS total_revenue
FROM Sales s
JOIN Products p ON s.product_id = p.product_id
GROUP BY 
  p.product_id,
  p.name,
  DATE_FORMAT(s.sale_date, '%Y-%m'),
  CONCAT(p.product_id, '-', DATE_FORMAT(s.sale_date, '%Y-%m'));
  
  
  DROP VIEW IF EXISTS monthly_investment;
CREATE VIEW monthly_investment AS
SELECT
  p.product_id,
  p.name AS product_name,
  i.month AS month,
  SUM(i.amount) AS total_investment
FROM Investment i
JOIN Products p ON i.product_id = p.product_id
GROUP BY p.product_id, i.month;
    
    
    


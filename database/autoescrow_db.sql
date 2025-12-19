/* =========================================
   AutoEscrow – Secure Vehicle Transaction System
   Database: autoescrow_db
   ========================================= */

DROP DATABASE IF EXISTS autoescrow_db;
CREATE DATABASE autoescrow_db;
USE autoescrow_db;

-- 1. ROLES
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

-- 2. USERS
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. USER_ROLES
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- 4. VEHICLES
CREATE TABLE vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT NOT NULL,
    brand VARCHAR(50),
    model VARCHAR(50),
    vehicle_year INT,
    price DECIMAL(10,2),
    description TEXT,
    status ENUM('AVAILABLE','IN_ESCROW','SOLD') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(user_id)
);

-- 5. VEHICLE_IMAGES
CREATE TABLE vehicle_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

-- 6. ESCROW_TRANSACTIONS
CREATE TABLE escrow_transactions (
    escrow_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    buyer_id INT NOT NULL,
    seller_id INT NOT NULL,
    escrow_status ENUM('PENDING','RELEASED','DISPUTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (buyer_id) REFERENCES users(user_id),
    FOREIGN KEY (seller_id) REFERENCES users(user_id)
);

-- 7. ESCROW_CONFIRMATIONS
CREATE TABLE escrow_confirmations (
    confirmation_id INT AUTO_INCREMENT PRIMARY KEY,
    escrow_id INT NOT NULL,
    buyer_confirm BOOLEAN DEFAULT FALSE,
    seller_confirm BOOLEAN DEFAULT FALSE,
    confirmed_at TIMESTAMP,
    FOREIGN KEY (escrow_id) REFERENCES escrow_transactions(escrow_id)
);

-- 8. WALLETS
CREATE TABLE wallets (
    wallet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 9. PAYMENTS
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    escrow_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('HELD','RELEASED','REFUNDED') DEFAULT 'HELD',
    transaction_ref VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (escrow_id) REFERENCES escrow_transactions(escrow_id)
);

-- 10. REPORTS
CREATE TABLE reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    escrow_id INT NOT NULL,
    reported_by INT NOT NULL,
    reason TEXT,
    status ENUM('OPEN','RESOLVED') DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (escrow_id) REFERENCES escrow_transactions(escrow_id),
    FOREIGN KEY (reported_by) REFERENCES users(user_id)
);

-- 11. ADMIN_ACTIONS
CREATE TABLE admin_actions (
    action_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    action_type VARCHAR(50),
    reference_id INT,
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(user_id)
);

-- Insert default roles
INSERT INTO roles (role_name) VALUES ('BUYER'), ('SELLER'), ('ADMIN');

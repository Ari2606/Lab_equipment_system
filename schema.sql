CREATE DATABASE IF NOT EXISTS lab_equipment_db;
USE lab_equipment_db;

CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(150) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    username VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS labs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL UNIQUE,
    department VARCHAR(120) NOT NULL,
    location VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS equipment_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    category_id BIGINT NOT NULL,
    lab_id BIGINT NOT NULL,
    availability_status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    condition_status VARCHAR(30) NOT NULL DEFAULT 'GOOD',
    notes VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_equipment_category FOREIGN KEY (category_id) REFERENCES equipment_categories(id),
    CONSTRAINT fk_equipment_lab FOREIGN KEY (lab_id) REFERENCES labs(id)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    approved_by BIGINT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    issue_time DATETIME NULL,
    actual_return_time DATETIME NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    purpose VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservation_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id),
    CONSTRAINT fk_reservation_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_reservation_approver FOREIGN KEY (approved_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS damage_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_id BIGINT NOT NULL,
    reported_by BIGINT NOT NULL,
    verified_by BIGINT NULL,
    reservation_id BIGINT NULL,
    description VARCHAR(500) NOT NULL,
    misuse_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_damage_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id),
    CONSTRAINT fk_damage_reported_by FOREIGN KEY (reported_by) REFERENCES users(id),
    CONSTRAINT fk_damage_verified_by FOREIGN KEY (verified_by) REFERENCES users(id),
    CONSTRAINT fk_damage_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE TABLE IF NOT EXISTS fines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    reservation_id BIGINT NULL,
    damage_report_id BIGINT NULL,
    amount DECIMAL(10,2) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_fine_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_fine_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    CONSTRAINT fk_fine_damage_report FOREIGN KEY (damage_report_id) REFERENCES damage_reports(id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    action VARCHAR(150) NOT NULL,
    details VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT IGNORE INTO permissions(code, description) VALUES
('EQUIPMENT_VIEW', 'View equipment'),
('EQUIPMENT_CREATE', 'Create equipment'),
('EQUIPMENT_UPDATE', 'Update equipment'),
('EQUIPMENT_DELETE', 'Delete equipment'),
('RESERVATION_CREATE', 'Create reservation'),
('RESERVATION_VIEW_OWN', 'View own reservations'),
('RESERVATION_VIEW_ALL', 'View all reservations'),
('RESERVATION_CANCEL_OWN', 'Cancel own reservations'),
('RESERVATION_APPROVE', 'Approve or reject reservations'),
('ISSUE_RETURN_PROCESS', 'Issue and return equipment'),
('DAMAGE_REPORT', 'Report damage'),
('DAMAGE_VIEW', 'View damage reports'),
('DAMAGE_VERIFY', 'Verify damage'),
('FINE_VIEW_OWN', 'View own fines'),
('FINE_MARK_PAID', 'Mark fine as paid'),
('ADMIN_USER_MANAGE', 'Manage users and roles'),
('REPORT_VIEW', 'View reports');

INSERT IGNORE INTO roles(name) VALUES
('STUDENT'),
('LAB_ASSISTANT'),
('ADMINISTRATOR');

INSERT IGNORE INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p
WHERE r.name = 'STUDENT'
AND p.code IN ('EQUIPMENT_VIEW','RESERVATION_CREATE','RESERVATION_VIEW_OWN','RESERVATION_CANCEL_OWN','DAMAGE_REPORT','FINE_VIEW_OWN');

INSERT IGNORE INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p
WHERE r.name = 'LAB_ASSISTANT'
AND p.code IN ('EQUIPMENT_VIEW','EQUIPMENT_CREATE','EQUIPMENT_UPDATE','RESERVATION_VIEW_ALL','RESERVATION_APPROVE','ISSUE_RETURN_PROCESS','DAMAGE_VIEW','DAMAGE_VERIFY','FINE_MARK_PAID');

INSERT IGNORE INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p
WHERE r.name = 'ADMINISTRATOR'
AND p.code IN ('EQUIPMENT_VIEW','EQUIPMENT_CREATE','EQUIPMENT_UPDATE','EQUIPMENT_DELETE','RESERVATION_VIEW_ALL','RESERVATION_APPROVE','ISSUE_RETURN_PROCESS','DAMAGE_VIEW','DAMAGE_VERIFY','FINE_MARK_PAID','ADMIN_USER_MANAGE','REPORT_VIEW');

INSERT IGNORE INTO labs(name, department, location) VALUES
('Physics Lab', 'Physics', 'Block A'),
('Chemistry Lab', 'Chemistry', 'Block B'),
('Computer Science Lab', 'Computer Science', 'Block C');

INSERT IGNORE INTO equipment_categories(name, description) VALUES
('Instrument', 'Scientific instruments'),
('Electronics', 'Electronic devices'),
('Tool', 'General lab tools'),
('Experimental Kit', 'Project and experiment kits');

-- Bootstrap first administrator manually.
-- Replace the password hash below with your own BCrypt hash if you want.
-- Example role_id lookup is done dynamically using subquery.
-- The app itself has no hardcoded users.
-- INSERT INTO users(full_name, email, username, password, status, role_id)
-- SELECT 'System Admin', 'admin@college.edu', 'admin', '$2a$10$replace_with_real_bcrypt_hash', 'ACTIVE', id
-- FROM roles WHERE name = 'ADMINISTRATOR';

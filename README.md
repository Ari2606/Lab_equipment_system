# Lab Equipment Reservation & Damage Tracking System

Spring Boot MVC mini-project for OOAD.

## Stack
- Java 17
- Spring Boot 4.0.5
- Spring MVC + Thymeleaf
- Spring Security with database-driven privileges
- Spring Data JPA + MySQL

## Features covered
- Login + registration
- Role-based, privilege-based authorization
- Equipment CRUD
- Reservation creation with overlap check
- Approval / rejection / issue / return flow
- Damage reporting and verification
- Fine creation for late return and misuse
- Admin user management
- Audit log + simple reports

## Important design choice
There are **no hardcoded users or hardcoded roles inside the business flow**.
Roles and permissions are stored in MySQL tables, and Spring Security reads privileges from the logged-in user's role.
Self-registration creates **Student** users only.
The first **Administrator** must be bootstrapped once using SQL.

## MySQL setup on Mac terminal
```bash
mysql -u root -p
```
Then run:
```sql
SOURCE /absolute/path/to/schema.sql;
```

## Update DB credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

## Run the app
```bash
mvn spring-boot:run
```
Then open:
```text
http://localhost:8080
```

## Suggested first steps after DB setup
1. Insert one admin user using the commented SQL in `schema.sql`.
2. Login as admin.
3. Create lab assistants or promote existing users.
4. Add equipment.
5. Test reservation flow with a student account.

## Suggested package explanation for viva
- `controller` -> handles web requests
- `service` -> business logic
- `repository` -> DB access layer
- `domain` -> JPA entities
- `config` -> security and auditing config
- `templates` -> Thymeleaf views

## Notes
- Fine amounts are currently fixed in service code (`100.00` late, `500.00` damage). If your faculty wants them policy-driven, move them to a `system_settings` table.
- Reports are kept simple for a mini-project. You can extend them with charts or export later.

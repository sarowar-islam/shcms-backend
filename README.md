# Student Hostel Complaint Management System - Backend

Spring Boot backend for the Student Hostel Complaint Management System (CUET-SHCMS).

## Features
- JWT-based authentication
- PostgreSQL database integration
- RESTful API endpoints
- Role-based access control
- Automated data seeding
- Health monitoring

## Tech Stack
- Java 21
- Spring Boot 4.1.1
- PostgreSQL 16
- JWT for authentication
- Maven for dependency management

## Setup Instructions

### Prerequisites
- Java 21 or later
- Maven 3.9+ 
- PostgreSQL 16+

### Database Setup
1. The system uses Neon PostgreSQL. Connection is already configured in `application.properties`
2. No manual database creation needed - JPA will create tables automatically

### Configuration
1. **Application Configuration**: Edit `src/main/resources/application.properties`
2. **Environment Variables**: See `.env` file for database credentials

### Running the Application

#### Development
```bash
./mvnw spring-boot:run
```

#### Production
```bash
./mvnw clean package
java -jar target/shcms.app-0.0.1-SNAPSHOT.jar
```

#### Docker
```bash
docker build -t shcms-backend .
docker run -p 8080:8080 shcms-backend
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Complaints
- `GET /api/complaints` - Get all complaints
- `GET /api/complaints/{id}` - Get complaint by ID
- `POST /api/complaints` - Create new complaint
- `PUT /api/complaints/{id}` - Update complaint
- `PATCH /api/complaints/{id}/status` - Update complaint status
- `PATCH /api/complaints/{id}/assign` - Assign complaint to staff
- `POST /api/complaints/{id}/updates` - Add update to complaint
- `DELETE /api/complaints/{id}` - Delete complaint

### System Configuration
- `GET /api/config` - Get system configuration
- `PUT /api/config` - Update system configuration

### Health Check
- `GET /api/health` - Health status and database connection

## Database Schema

### Users Table
- id (PK, auto-increment)
- user_id (unique)
- username (unique)
- password (hashed)
- role (STUDENT, STAFF, ADMIN)
- name, email, room, department, phone
- joined_date, active

### Complaints Table
- id (PK, auto-increment)
- complaint_id (unique)
- student_id, student_name, room
- title, description
- category, urgency, status
- assigned_to, submitted_at, updated_at

### Complaint Updates Table
- id (PK, auto-increment)
- update_id (unique)
- message, by_user, by_name, timestamp
- complaint_id (FK)

### System Config Table
- id (PK)
- hall_name, total_rooms, warden_name
- contact_email, notifications_enabled
- auto_assign, max_complaints_per_day

## Seeded Data

On first run, the system automatically seeds:

### Users
- Admin: admin/admin
- Staff: staff1/staff1, staff2/staff2, staff3/staff3
- Students: 2204107/student, 2204048/student, etc.

### Complaints
- 8 sample complaints with updates
- Various categories and statuses

## Security

### JWT Configuration
- Secret key: Configurable in application.properties
- Expiration: 24 hours by default
- Header: `Authorization: Bearer <token>`

### CORS Configuration
Allowed origins:
- http://localhost:5173 (Vite dev server)
- http://localhost:3000 (Alternative)
- http://localhost:8443 (Figma Make)

## Testing

Run tests:
```bash
./mvnw test
```

## API Examples

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### Create Complaint
```bash
curl -X POST http://localhost:8080/api/complaints \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "2204107",
    "studentName": "Sarowar Islam",
    "room": "A-204",
    "title": "Test Complaint",
    "description": "This is a test complaint",
    "category": "ELECTRICAL",
    "urgency": "MEDIUM"
  }'
```

### Health Check
```bash
curl http://localhost:8080/api/health
```

## Troubleshooting

### Database Connection Issues
1. Check PostgreSQL credentials in `.env`
2. Verify network connectivity to Neon PostgreSQL
3. Ensure database exists and user has proper permissions

### Build Issues
1. Ensure Java 21 is installed: `java -version`
2. Check Maven: `mvn -version`
3. Clean and rebuild: `./mvnw clean package`

### Runtime Issues
1. Check logs in `target/logs/` directory
2. Verify application.properties configuration
3. Check port 8080 is available

## Deployment

### Environment Variables
Required:
- `DATABASE_URL`: PostgreSQL connection string
- `JWT_SECRET`: JWT signing key (production only)

### Production Settings
1. Change `spring.jpa.hibernate.ddl-auto` to `validate`
2. Set `jwt.secret` to strong random value
3. Enable production logging configuration
4. Configure SSL/TLS for production

## Monitoring
- Health endpoint: `/api/health`
- Application metrics: `/actuator/health`
- Database connection pooling metrics

## Frontend Integration
Backend is configured to work with the React frontend:
- Base URL: http://localhost:8080/api
- CORS enabled for frontend origins
- JWT authentication compatible

## License
Proprietary - CUET Student Hostel Management System
```


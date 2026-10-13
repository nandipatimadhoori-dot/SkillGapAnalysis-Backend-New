# AI Skill Gap Analysis Backend (Spring Boot + MySQL)

## Requirements
- Java 21
- IntelliJ IDEA
- MySQL Server / MySQL Workbench
- Internet connection for Maven dependencies

## Setup
1. Open this folder in IntelliJ as a Maven project.
2. Create the database in MySQL Workbench:
   ```sql
   CREATE DATABASE IF NOT EXISTS skillgap_db;
   ```
3. Edit `src/main/resources/application.properties` and replace `YOUR_MYSQL_PASSWORD` with the MySQL root password. Alternatively set environment variable `DB_PASSWORD`.
4. Reload Maven and run `SkillGapAnalysisBackendApplication.java`.
5. Check `http://localhost:8080/api/health`.

## API quick reference
- `GET /api/health` health check
- `POST /api/auth/register` JSON: `{ "fullName":"Student Name", "email":"student@example.com", "password":"StrongPass123", "role":"STUDENT" }`
- `POST /api/auth/login` JSON: `{ "email":"student@example.com", "password":"StrongPass123" }`
- `POST /api/auth/logout`
- `PUT /api/profiles/{userId}` JSON: `{ "education":"B.Tech CSE", "experience":"Beginner", "careerInterest":"Java Backend Developer", "skills":"Java, SQL, Git" }`
- `GET /api/profiles/{userId}`
- `GET /api/analysis/{userId}` rule-based job recommendations and missing skills
- `POST /api/resume/{userId}` multipart form field `file` with a PDF
- `GET /api/learning/path?role=Java%20Backend%20Developer`
- `GET /api/tasks` and `GET /api/tasks/open`
- `POST /api/tasks` create a task; example `{ "title":"Build a portfolio page", "description":"Create a responsive page", "requiredSkills":"HTML,CSS", "rewardAmount":500, "companyUserId":1 }`
- `POST /api/tasks/{taskId}/submit` JSON: `{ "studentUserId":1, "submissionUrl":"https://github.com/example/repo", "notes":"Completed" }`
- `GET /api/tasks/submissions/student/{userId}`
- `PUT /api/tasks/submissions/{submissionId}/approve`
- `GET /api/tasks/earnings/{userId}`

## Important security limitation
This is a functional development MVP, not production-ready. Passwords are hashed with BCrypt, but JWT authentication and per-user authorization are not yet enforced: the current API is permissive to make initial local testing easier. Do not deploy publicly or store real student/company data until token validation, role checks, ownership checks, upload limits, and payment verification are implemented. Earnings are tracking records only; no actual payment is processed.

## AI limitation
Job suitability is currently a transparent skill-overlap heuristic, not a trained AI/ML model. Resume upload extracts text from text-based PDFs; scanned-image PDFs need OCR.

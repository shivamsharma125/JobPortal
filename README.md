# 💼 Job Search & Recruitment Platform

A feature-rich and scalable **Job Portal Backend** built with **Spring Boot**, enabling job seekers to explore opportunities and apply, while recruiters can manage postings efficiently. Includes advanced **search**, **authentication**, **notifications**, and **admin analytics**.

---

## 🚀 Key Features

### 👨‍💻 Applicant & Recruiter Workflows

* Register/login via **JWT**
* **Applicants** can:

    * Search & filter jobs (skills, experience, salary, location, etc.)
    * Bookmark jobs
    * Apply to jobs
    * Track application status
* **Recruiters** can:

    * Post, update, delete jobs
    * View applications received
    * Set application statuses

### 🔍 Intelligent Job Search

* Powered by **Elasticsearch** with:

    * **Full-text search** (title, skills, location)
    * **Advanced filtering** (experience, salary, job type, remote)
    * **Sorting** and **pagination**
* Automatic **fallback to SQL** if Elasticsearch is unavailable

### 🔐 Secure Authentication & Authorization

* **JWT-based stateless security**
* **Role-based access control** (ADMIN / RECRUITER / APPLICANT)
* Passwords hashed using **BCrypt**

### 📧 Async Email Notifications

* **Kafka-backed async messaging**
* Emails sent on:

    * New job applications
    * Application status updates
* Integrated with **SMTP** using **JavaMail**

### 📊 Admin Dashboard APIs

* Analytics for platform metrics:

    * Total users, jobs, and applications
    * Applications per day (for charting)
    * Jobs per recruiter
* Built-in support for **future dashboard UI integration**

---

## 🏗️ Tech Stack

| Layer         | Technology                      |
| ------------- |---------------------------------|
| Language      | Java 17                         |
| Framework     | Spring Boot 3                   |
| Database      | MySQL + **Flyway** (migrations) |
| Search Engine | **Elasticsearch**               |
| Messaging     | **Apache Kafka**                |
| Email         | JavaMail                        |
| Auth          | Spring Security + JWT |

---

## 🗂️ Modules

* **User Service** – Registration, login, role management
* **Job Service** – Job CRUD, Elasticsearch sync
* **Search Service** – Elasticsearch filtering + fallback
* **Job Application Service** – Apply, status change, notification trigger
* **Bookmark Service** – Save/remove/get all bookmarked jobs
* **Analytics Service** – Admin insights

---

## 📌 API Overview

### 🔐 AuthController

* `POST /auth/signup` – Register a new user (Applicant or Recruiter)
* `POST /auth/login` – Authenticate user and receive JWT token

### 🧑‍💼 JobController

* `POST /job` – Create a new job (Recruiter only)
* `GET /job` – Get all active jobs (Applicant only)
* `GET /job/my` – Get jobs posted by logged-in recruiter (Recruiter only)
* `PUT /job/{jobId}` – Update existing job (Recruiter only)
* `DELETE /job/{jobId}` – Delete job (Recruiter only)

### 📄 JobApplicationController

* `POST /job/applications` – Apply to a job (Applicant only)
* `GET /job/applications` – View applications received for recruiter's jobs (Recruiter only)
* `PATCH /job/applications/{id}/status` – Update application status (Recruiter only)

### 💾 JobBookmarkController

* `POST /bookmarks/{jobId}` – Bookmark a job (Applicant only)
* `DELETE /bookmarks/{jobId}` – Remove bookmarked job (Applicant only)
* `GET /bookmarks` – View all bookmarked jobs (Applicant only)

### 🔍 SearchController

* `GET /search` – Search jobs using filters: skill, location, remote, salary, experience, job type, etc. (supports sorting, pagination)

### 📊 AdminAnalyticsController

* `GET /admin/analytics/overview` – Get job, application, and user summary (Admin only)
* `GET /admin/analytics/jobs-per-recruiter` – List recruiters and their job count (Admin only)
* `GET /admin/analytics/applications-per-day?days=N` – Application counts grouped by day for the past N days (Admin only)

---

## 🔎 Search API Filters Supported

* `skills`: List<String>
* `location`: String
* `isRemote`: Boolean
* `jobType`: String (e.g., FULL\_TIME, PART\_TIME)
* `experienceLevel`: String (e.g., JUNIOR, MID, SENIOR)
* `minSalary` / `maxSalary`: Double
* `minExperience` / `maxExperience`: Integer
* `sortBy`: postedAt / salary
* `direction`: asc / desc
* `page`, `size`: for pagination

---

## 📬 Email Notifications

Emails are sent asynchronously using **Kafka**:

* On successful job application – confirmation email to applicant
* On status change – email notification about rejection/shortlisting

---

## 🔄 Database Migrations

* Managed using **Flyway**
* All schema changes version-controlled
* Run automatically on app startup

---

## 📁 Folder Structure Overview

```
jobportal/
├── advices/
├── clients/
├── configs/
├── controllers/
├── dtos/
├── exceptions/
├── models/
├── repositories/
├── security/
├── services/
├── utils/
```

---


## 🏁 Future Enhancements

* Resume uploads & recruiter shortlisting
* Notification preferences (SMS, email, in-app)
* User profile enrichment (skills, resume parser)
* Integration with Calendar APIs to streamline slot booking and meeting generation.
* Implement OAuth2 to allow users to sign up and log in using their Google, LinkedIn, or GitHub accounts.
* Automatically extract details like skills, education, and experience from uploaded PDF resumes to auto-fill the user profile.
---

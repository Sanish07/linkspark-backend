# Linkspark - The URL Shortener Application

## 📌 Overview

This application provides a **URL shortening service** with detailed analytics for tracking link clicks over time. It includes a **React-based frontend** and a **Spring Boot-powered backend**, ensuring a seamless and efficient experience for users.

---

## 🚀 Features

- 🔗 **Shorten Long URLs**: Generate compact and shareable links.
- 📊 **Click Analytics**: Track link usage with a detailed statistics dashboard.
- 🔒 **Secure Authentication**: JWT Token-based authorization.
- ⏳ **Session Management**: Ensures secure and persistent user sessions with token-based authentication.
- ☁️ **Cloud Deployment**: Hosted with **Docker and various cloud services**.

---

## 🏗️ Technologies Used

### 🔹 **Frontend (React + Vite)**
- **React.JS** – UI development.
- **React Query** – Data fetching and caching.
- **React Router** – Navigation management.
- **Tailwind CSS** – Modern, responsive styling.
- **Chart.js** – Click statistics visualization.
- **React Icons** – Beautiful icons for UI enhancement.
- **<em>And Others...<em>**

### 🔹 **Backend (Spring Boot)**
- **Spring Boot** – REST API development.
- **Spring Security** – Authentication & authorization.
- **Spring Data JPA** – Database ORM.
- **PostgreSQL** – Persistent database storage.
- **JWT** – Secure API authentication.
- **Lombok** – Boilerplate code reduction.

### 🔹 **Deployment & DevOps**
- **Docker** – For Containerization.
- **Cloud Providers (i.e., Neon, Render, Netlify)** – Hosting services.

---

## 🎯 How to Run Locally

### 📦 Backend
```bash
# Clone the backend repository
git clone <backend-repo-url>
cd backend

# Configure environment variables in .env file
# Set up run configuration by locating & setting .env file at runtime 

# Build and run
./mvnw spring-boot:run
```

### 💻 Frontend
```bash
# Clone the frontend repository
git clone <frontend-repo-url>
cd frontend

# Install dependencies
npm install

# [Important]Create a .env file at root location and set the variables
VITE_SERVER_URL=http://localhost:<Backend_Port>
VITE_CLIENT_URL=http://localhost:<Frontend_Port>

# Run the app
npm run dev
```

---

## 🌍 Hosting & Deployment
- **Backend**: Hosted on `Render`
- **Frontend**: Deployed on `Netlify`
- **Database**: PostgreSQL hosted on `Neon`

The application is hosted on **Netlify** for testing and exploring. You can access it here:  

🔗 **Live URL:** [https://linkspark.netlify.app](https://linkspark.netlify.app/)  

Netlify provides seamless CI/CD, automatic deployments from the Git repository, and optimizations for fast performance.


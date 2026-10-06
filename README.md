# ResolveX — Intelligent Ticket Resolution System

ResolveX is a full-stack AI-assisted customer support ticket platform.

## Features

- Create and track customer support tickets
- Dashboard with ticket statistics
- Filter tickets by status and priority
- Spring Boot REST API
- MySQL persistence with JPA
- Python AI service for NLP-style ticket classification and suggested replies
- ReactJS frontend
- CORS configured for local development
- Docker Compose support for MySQL and the AI service
- Health endpoints for backend and AI service

## Architecture

```text
ReactJS Frontend
       |
       v
Spring Boot REST API  ----->  MySQL
       |
       v
Python AI Service
```

The AI service intentionally uses a lightweight, deterministic NLP/rule-based approach so the project can run without a paid API key. You can later replace the classifier with an LLM provider.

## Project structure

```text
ResolveX/
├── frontend/       # React + Vite
├── backend/        # Spring Boot + JPA
├── ai-service/     # Python Flask NLP service
├── database/       # MySQL schema
├── docker-compose.yml
└── README.md
```

## Requirements

- Java 17+
- Maven 3.9+
- Node.js 20+
- npm
- Python 3.10+
- MySQL 8+

## 1. Database

Create a MySQL database:

```sql
CREATE DATABASE resolvex;
```

Then run:

```bash
mysql -u root -p resolvex < database/schema.sql
```

Or let Hibernate create the tables automatically using the default configuration.

## 2. Start AI service

```bash
cd ai-service
python -m venv venv
```

Windows:

```bash
venv\Scripts\activate
```

macOS/Linux:

```bash
source venv/bin/activate
```

Install dependencies:

```bash
pip install -r requirements.txt
python app.py
```

AI service runs at:

`http://localhost:5001`

## 3. Start Spring Boot backend

Open a new terminal:

```bash
cd backend
mvn spring-boot:run
```

Backend runs at:

`http://localhost:8080`

Health check:

`http://localhost:8080/api/health`

## 4. Start React frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the URL printed by Vite, normally:

`http://localhost:5173`

## API examples

### Create ticket

`POST /api/tickets`

```json
{
  "customerName": "Aarav Sharma",
  "email": "aarav@example.com",
  "subject": "Payment failed",
  "description": "My payment was declined even though the amount was deducted."
}
```

### List tickets

`GET /api/tickets`

### Get ticket

`GET /api/tickets/{id}`

### Update ticket status

`PATCH /api/tickets/{id}/status`

```json
{
  "status": "IN_PROGRESS"
}
```

### AI analyze

`POST /api/tickets/{id}/analyze`

The backend sends the ticket description to the Python service and stores the returned category, priority and suggested reply.

## Resume description

**ResolveX — Intelligent Ticket Resolution System**

- Developed an AI-assisted support platform that analyzes customer queries and generates relevant response suggestions using NLP and Generative-AI-ready architecture.
- Built a ReactJS interface and Spring Boot REST backend for ticket creation, tracking, filtering and automated query processing.
- Integrated a Python NLP service with MySQL persistence to classify tickets by category/priority and generate contextual reply suggestions.

## Important

This repository contains a runnable portfolio implementation. The AI service uses local NLP rules rather than pretending to call a paid Generative AI API. For a production version, replace `ai-service/app.py`'s classifier with an LLM provider and keep secrets in environment variables.

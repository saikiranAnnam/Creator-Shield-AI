# 🎧 CreatorShield

CreatorShield is a backend-first trust and verification platform for creator uploads.

It detects suspicious metadata, flags duplicate or impersonation-like submissions, computes a trust score, and exposes alerts for high-risk uploads.

The project is inspired by problems modern content platforms face around policy, accountability, trust, and AI-generated content.

---

## Table of contents

- [Why I built this](#why-i-built-this)
- [Overview](#overview)
- [Problem](#problem)
- [Solution](#solution)
- [Core features](#core-features)
- [Architecture](#architecture)
- [Tech stack](#tech-stack)
- [API (Phase 1)](#api-phase-1)
- [Run locally](#run-locally)
  - [Prerequisites](#prerequisites)
  - [1. Infrastructure](#1-infrastructure)
  - [2. Backend](#2-backend)
  - [3. Frontend](#3-frontend)
  - [Quick API check](#quick-api-check)
  - [Stop](#stop)
- [Configuration](#configuration)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Trust scoring (summary)](#trust-scoring-summary)
- [Project layout](#project-layout)
- [License](#license)

---

## Why I built this

Large creator platforms need more than basic schema validation during uploads.
They need systems that can answer questions like:

Is this upload a duplicate or near-duplicate?
Does this metadata look suspicious?
Is this creator behaving abnormally?
Should this upload be approved automatically or flagged for review?

CreatorShield adds a trust layer to the upload pipeline.

---

## Overview

CreatorShield sits between **upload ingestion** and **publish / review** workflows. It focuses on:

- Duplicate and near-duplicate metadata (normalized + title similarity)
- Rule-based **trust score** and **reason codes** (auditable decisions)
- **Kafka** so the HTTP API stays fast while validation runs in the background
- A **Next.js** dashboard for uploads, validation lookup, and suspicious alerts

Non-goals for MVP: audio fingerprinting, legal ownership proof, or fully automated moderation without humans.

---

## 🚨 Problem

Modern content platforms face:

- Duplicate or near-duplicate uploads  
- Impersonation / fake creator accounts  
- Manipulated metadata  
- AI-generated or misleading content  

Traditional pipelines validate format — **not trust**.

---

## 🎯 Solution

CreatorShield adds an **asynchronous trust layer** to the upload pipeline:

- Verifies metadata integrity  
- Detects duplicate / similar content  
- Evaluates creator behavior  
- Computes a **trust score (low / medium / high risk)**  
- Flags suspicious uploads for review  

## 🔑 Core Features

### 1. Upload API
- Accepts creator metadata  
- Stores upload in database  
- Publishes event to Kafka  

### 2. Async Validation Pipeline
- Kafka decouples ingestion and validation  
- Consumer processes uploads asynchronously  

### 3. Duplicate / Similarity Detection
- Metadata normalization  
- Jaro–Winkler similarity  
- Duplicate detection across creators  

### 4. Trust Scoring
Signals used:
- duplicate similarity  
- creator history  
- upload frequency  
- metadata anomalies  

**Output:**
- Low → approve  
- Medium → review  
- High → alert  

### 5. Suspicious Alerts
- High-risk uploads are flagged  
- Stored with reason codes for explainability  

## Architecture

| Piece | Role |
|--------|------|
| **Spring Boot** | REST API for uploads, validations, and alerts |
| **PostgreSQL** | Creators, uploads, validation results, alert records |
| **Kafka** | `upload.created` events decouple ingest from verification |
| **Consumer** | Loads upload, runs duplicate/similarity checks + trust scoring, persists results |
| **Next.js** | Calls the same REST API (`NEXT_PUBLIC_API_BASE_URL`) |

**Flow:** `POST /uploads` → DB + `PENDING_VALIDATION` → Kafka → consumer → validation result + optional suspicious alert → status updated.

---

## Tech stack

| Area | Technology |
|------|------------|
| Backend | Java 17, Spring Boot 3.x, Spring Data JPA, Spring Kafka |
| Database | PostgreSQL (Flyway migrations) |
| Messaging | Apache Kafka + Zookeeper (Docker Compose) |
| Frontend | Next.js (App Router), TypeScript, Tailwind CSS |
| Tooling | Maven, Docker Compose |

---

## API (Phase 1)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/uploads` | Create upload (metadata); returns `PENDING_VALIDATION` |
| `GET` | `/api/v1/uploads/{uploadId}` | Upload details and status |
| `GET` | `/api/v1/validations/{uploadId}` | Trust score, risk level, decision, reason codes |
| `GET` | `/api/v1/alerts/suspicious` | Suspicious alerts (optional query filters) |

Base URL: `http://localhost:8080` by default.

---

## Run locally

### Prerequisites

- Docker (for Postgres + Kafka)
- Java 17 + Maven (backend)
- Node.js + npm (frontend)

### 1. Infrastructure

From the **repository root**:

```bash
docker compose up -d
```

This starts PostgreSQL, Zookeeper, and Kafka (see `docker-compose.yml`).

### 2. Backend

```bash
mvn spring-boot:run
```

API: `http://localhost:8080`

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

App: `http://localhost:3000`

See `frontend/README.md` for UI-specific notes.

### Quick API check

```bash
curl -s -X POST http://localhost:8080/api/v1/uploads \
  -H 'Content-Type: application/json' \
  -d '{
    "creatorId":"demo_creator_1",
    "creatorName":"Demo Artist",
    "songTitle":"Midnight Lights",
    "artistName":"Demo Artist",
    "genre":"Pop"
  }'
```

Then poll `GET /api/v1/uploads/{id}` and `GET /api/v1/validations/{id}` until validation completes.

### Stop

- Stop Spring Boot: `Ctrl+C`
- Stop frontend dev server: `Ctrl+C`
- Stop containers: `docker compose down`

---

## Configuration

### Backend

Defaults live in `src/main/resources/application.yml` (JDBC URL, Kafka bootstrap, Flyway).

### Frontend

| Variable | Purpose |
|----------|---------|
| `NEXT_PUBLIC_API_BASE_URL` | Backend base URL for the browser (default `http://localhost:8080/api/v1`) |

Example:

```bash
export NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
cd frontend && npm run dev
```

---

## Trust scoring (summary)

Scoring is **rule-based** and **deterministic** (not ML in Phase 1). Signals include exact duplicate under another creator, high title similarity (e.g. Jaro–Winkler), creator history, new account age, upload rate, and metadata completeness. Outputs map to risk bands and decisions (e.g. auto-approved vs review vs suspicious alert). Reason codes are stored for auditability.

---

## Project layout

```
creatorSheild/
├── pom.xml                 # Maven / Spring Boot
├── docker-compose.yml      # Postgres + Kafka + Zookeeper
├── src/main/java/...       # Backend
├── src/main/resources/     # application.yml, Flyway migrations
└── frontend/               # Next.js app (see frontend/README.md)
```

## License

Add a license file if you open-source the repo (e.g. MIT).
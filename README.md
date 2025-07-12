# Device Manager Application

[![License](https://img.shields.io/github/license/samuelpmvm/device-manager)](LICENSE)

## Overview
Device Manager Application is a REST API built with Spring Boot to manage device resources. 
It provides endpoints to perform CRUD operations on devices.

## Features
- Endpoints to manage devices.
- Uses PostgreSQL database for data storage.
- Docker and Docker Compose support for containerized deployment.
- Spring Actuator integration for health metrics and monitoring.
- Integrated with GitHub Actions for CI/CD pipeline.

## Technologies Used
- Java
- Spring Boot
- Maven
- Docker
- PostgreSQL

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.9 or higher

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/samuelpmvm/device-manager.git

## 🚀 Running Locally

### Requirements

- Docker
- Docker Compose

### Start the app

```bash
  docker compose -f docker-compose.yaml up -d --build
```


## 🧾 API Overview

All endpoints are prefixed with: /device-manager/api/v1

```
http://localhost:8080/device-manager/swagger-ui/index.html
```

📱 Devices
- `POST /devices`: Create a new device.
- `PUT /devices/{id}`: Fully update an existing device.
- `PATCH /devices/{id}`: Partially update an existing device.
- `GET /devices/{id}`: Fetch a single device.
- `GET /devices`: Fetch all devices. 🚧
- `GET /devices/brand/{brand}`: Fetch devices by brand. 🚧
- `GET /devices/state/{state}`: Fetch devices by state. 🚧
- `DELETE /devices/{id}`: Delete a single device. 🚧

## 🧪 Running Tests

Run all tests
```bash
  ./mvnw test
```

Run unit tests
```bash
  ./mvnw test -Dgroups=unit
```

Run integration tests
```bash
  ./mvnw test -Dgroups=integration
```


## 🛠 GitHub Actions CI/CD

Pipeline steps:
- Build the application using Maven.
- Build the Docker image, run docker compose and wait for the application to be ready.
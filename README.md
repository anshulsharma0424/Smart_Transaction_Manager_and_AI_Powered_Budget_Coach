Of course. A great project deserves a great `README.md` file. This file will serve as the front door to your project on GitHub, explaining what it is, its features, its architecture, and how to run it.

Here is a complete, professional `README.md` file for your Spenzr application.

-----

# Spenzr: Smart Transaction Manager & AI Budget Coach 💸

Spenzr is a comprehensive, full-stack personal finance application designed to provide users with a seamless experience for managing their finances. It combines a personal transaction manager, a group expense splitter, and an AI-powered budget coach into a single, cohesive platform. The entire application is built from the ground up using a modern, scalable microservices architecture.

## ✨ Key Features

The application is divided into three core modules:

### Part A: Smart Transaction Manager

  * **Personal Tracking:** Full CRUD (Create, Read, Update, Delete) functionality for personal income and expense transactions.
  * **Categorization:** Manage custom categories for transactions (e.g., Food, Shopping, Bills).
  * **Reporting:** A simple reporting feature to see total income, expenses, and net balance.
  * **Personalization:** Users can set their preferred currency (INR/USD).

### Part B: Group Expense Splitter

  * **Event Management:** Create and manage group events for trips, parties, or shared living.
  * **Participant Management:** Easily add or remove other Spenzr users to an event.
  * **Group Expenses:** Log expenses within an event, specifying who paid and how the cost is split.
  * **Debt Settlement:** A sophisticated algorithm calculates the simplest way for everyone to settle their debts, showing exactly who owes whom.

### Part C: AI Budget Coach

  * **Asynchronous Processing:** Personal and group expenses are published to an **Apache Kafka** topic for asynchronous analysis.
  * **AI-Powered Insights:** A dedicated AI service consumes the expense data and uses **Google Gemini** to generate personalized, actionable financial tips.
  * **Suggestion Hub:** All generated tips are stored and can be viewed on a dedicated "AI Coach" page.

## 🏗️ Architecture

Spenzr is built on a distributed, granular microservices architecture to ensure loose coupling, scalability, and independent development. All services are registered with a discovery server and communicate via an API Gateway or an asynchronous message broker.

```mermaid
graph TD
    subgraph Frontend
        ReactApp[React.js Frontend]
    end

    subgraph Infrastructure
        Gateway[API Gateway]
        Eureka[Eureka Discovery Server]
        Config[Config Server]
        Kafka[Apache Kafka]
        Keycloak[Keycloak Auth Server]
    end

    subgraph "Part A: Personal Finance"
        UserService[User Service <br> (PostgreSQL)]
        TransactionService[Transaction Service <br> (PostgreSQL)]
        CategoryService[Category Service <br> (MongoDB)]
        ReportingService[Reporting Service]
        UserPreferenceService[User Preference Service <br> (PostgreSQL)]
    end

    subgraph "Part B: Group Expenses"
        EventService[Event Service <br> (MongoDB)]
        GroupExpenseService[Group Expense Service <br> (MongoDB)]
        SettlementService[Settlement Service]
    end

    subgraph "Part C: AI Coach"
        DataAggregationService[Data Aggregation Service]
        AIInferenceService[AI Inference Service <br> (Gemini API)]
        SuggestionService[Suggestion Service <br> (MongoDB)]
    end

    ReactApp -- REST API --> Gateway;
    Gateway -- REST API --> UserService;
    Gateway -- REST API --> TransactionService;
    Gateway -- REST API --> CategoryService;
    Gateway -- REST API --> ReportingService;
    Gateway -- REST API --> UserPreferenceService;
    Gateway -- REST API --> EventService;
    Gateway -- REST API --> GroupExpenseService;
    Gateway -- REST API --> SettlementService;
    Gateway -- REST API --> SuggestionService;

    TransactionService -- Publishes Event --> Kafka;
    GroupExpenseService -- Publishes Event --> Kafka;
    Kafka -- Consumes Event --> DataAggregationService;
    DataAggregationService -- REST API --> AIInferenceService;
    DataAggregationService -- REST API --> SuggestionService;
    
    SettlementService -- REST API --> GroupExpenseService;
    ReportingService -- REST API --> TransactionService;
    EventService -- REST API --> UserService;

    classDef db fill:#E8E8E8,stroke:#333,stroke-width:2px;
    class UserService,TransactionService,UserPreferenceService,CategoryService,EventService,GroupExpenseService,SuggestionService db;

    %% Service Discovery Links
    UserService & TransactionService & CategoryService & ReportingService & UserPreferenceService & EventService & GroupExpenseService & SettlementService & DataAggregationService & AIInferenceService & SuggestionService & Gateway & Config -- Registers with --> Eureka;
    %% Configuration Links
    UserService & TransactionService & CategoryService & ReportingService & UserPreferenceService & EventService & GroupExpenseService & SettlementService & DataAggregationService & AIInferenceService & SuggestionService & Gateway -- Fetches Config --> Config;
```

## 🛠️ Technology Stack

| Category         | Technology                                                                          |
| ---------------- | ----------------------------------------------------------------------------------- |
| **Backend** | Java 21, Spring Boot 3, Spring Cloud, Spring Security, Spring Kafka                 |
| **Frontend** | React.js, Vite, Tailwind CSS, React Router, Zustand                                 |
| **Databases** | PostgreSQL (for relational data), MongoDB (for document data)                       |
| **Messaging** | Apache Kafka                                                                        |
| **Security** | OAuth2 / OIDC with Keycloak                                                         |
| **AI** | Google Gemini API, `WebClient` for direct integration                               |
| **Infrastructure** | Docker & Docker Compose, Eureka (Service Discovery), Spring Cloud Gateway, Spring Cloud Config |
| **Build Tool** | Maven                                                                               |

## 🚀 Getting Started

Follow these steps to get the entire Spenzr application running on your local machine.

### Prerequisites

  * Java (JDK 21+)
  * Maven
  * Node.js & npm
  * Docker & Docker Compose

### 1\. Clone the Repositories

You will need to clone the main repository containing all the services and your separate configuration repository.

### 2\. Configure Secrets

The application requires a **Google Gemini API Key**.

  * Get your key from [Google AI Studio](https://aistudio.google.com/).
  * Set it as an environment variable on your system or in your IDE's run configuration:
    ```bash
    export GOOGLE_API_KEY="your-real-api-key-here"
    ```

### 3\. Backend Startup Order

The microservices must be started in a specific order.

1.  **Start Infrastructure with Docker Compose:**
      * Navigate to the directory with your `docker-compose.yml` file.
      * Run `docker-compose up -d`. This will start **Kafka** and **Keycloak**.
2.  **Start Core Cloud Services:**
      * Run the **`eureka-server`** application and wait for it to start.
      * Run the **`config-server`** application and wait for it to start.
3.  **Start All Other Backend Services:**
      * You can now start all the other backend microservices in any order (`user-service`, `transaction-service`, etc.).

### 4\. Frontend Setup

1.  **Navigate to the frontend directory:**
    ```bash
    cd spenzr-frontend
    ```
2.  **Install dependencies:**
    ```bash
    npm install
    ```
3.  **Create an environment file:**
      * Create a file named `.env` in the `spenzr-frontend` root.
      * Add the following content:
        ```
        VITE_KEYCLOAK_URL=http://localhost:8180
        VITE_KEYCLOAK_REALM=spenzr-realm
        VITE_KEYCLOAK_CLIENT_ID=spenzr-react-client
        VITE_API_BASE_URL=http://localhost:8080/api/v1
        ```
4.  **Run the frontend:**
    ```bash
    npm run dev
    ```
5.  Open your browser and navigate to `http://localhost:5173`. You will be redirected to the Keycloak login page.

-----

This `README.md` file provides a comprehensive and professional overview of the Spenzr project.

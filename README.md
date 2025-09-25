# AeroVista - Aviation Management System

AeroVista is an online system that helps airlines and aviation companies manage their work easily.
It provides modules for flight sales, booking, business intelligence, investments, feedback, and chatbot support — all in one integrated platform.

## 🌟 Features

- **Flight Management**: Track and manage flight schedules, routes, and status
- **Sales & Booking**: Handle ticket sales and reservations
- **Business Intelligence**: Gain insights with analytics and reporting
- **User Feedback**: Collect and manage customer feedback
- **Investment Tracking**: Monitor and manage investments and financials
- **Chatbot Support**: AI-powered customer support
- **Responsive Design**: Works on desktop and mobile devices

## 🛠️ Technologies Used

- **Frontend**:
  - JSP (JavaServer Pages)
  - HTML5, CSS3, JavaScript
  - Bootstrap (for responsive design)
  
- **Backend**:
  - Java EE
  - Servlets
  - JDBC for database connectivity
  
- **Database**:
  - MySQL
  
- **Libraries & Tools**:
  - Gson 2.10.1 (JSON processing)
  - JSTL 1.2 (JavaServer Pages Standard Tag Library)
  - MySQL Connector/J 8.0.31

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Tomcat 9.0 or higher
- MySQL Server 5.7 or higher
- Maven 3.6 or higher (for dependency management)

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Rubigupta358/Aerovista.git
   cd AeroVista
   ```

2. **Database Setup**:
   - Import the database schema from `AeroVista_Schema.sql`
   - Update the database configuration in `WEB-INF/classes/config.properties`

3. **Build the Project**:
   ```bash
   mvn clean package
   ```
   
4. **Deploy to Tomcat**:
   - Copy the generated WAR file to Tomcat's `webapps` directory
   - Start the Tomcat server

5. **Access the Application**:
   Open your browser and navigate to `http://localhost:8080/AeroVista`

## 📂 Project Structure

```
AeroVista/
│
├─ src/
│   └─ main/
│       ├─ java/
│       │   ├─ Servlets/
│       │   │   ├─ dao/                     ← DAO layer (DB operations)
│       │   │   │   ├─ FeedbackDAO.java
│       │   │   │   ├─ InvestmentDAO.java
│       │   │   │   ├─ ProductDAO.java
│       │   │   │   ├─ ProductDAOTest.java
│       │   │   │   └─ SalesDAO.java
│       │   │   │
│       │   │   ├─ model/                   ← POJOs (DB entities)
│       │   │   │   ├─ Feedback.java
│       │   │   │   ├─ Investment.java
│       │   │   │   ├─ Product.java
│       │   │   │   └─ Sale.java
│       │   │   │
│       │   │   └─ web/                     ← Servlet controllers
│       │   │       ├─ AIChatServlet.java
│       │   │       ├─ ChartDataServlet.java
│       │   │       ├─ FeedbackServlet.java
│       │   │       ├─ InvestmentServlet.java
│       │   │       ├─ ProductServlet.java
│       │   │       └─ SaleServlet.java
│       │   │
│       │   └─ ConnectDB.java               ← DB connection utility
│       │
│       └─ resources/                        ← Config files
│           └─ config.properties
│
└─ WebContent/                               ← JSP pages + static resources
    ├─ bi.jsp                                ← Business Insights page
    ├─ index.jsp                              ← Home page
    ├─ navbar.jsp                             ← Navbar include
    ├─ chatbot.jsp                            ← Chatbot UI
    ├─ feedback.jsp                           ← Feedback page
    ├─ investment.jsp                         ← Investment page
    ├─ productForm.jsp                        ← Product add form
    ├─ sales.jsp                              ← Sales page
    ├─ style.css                              ← Common CSS
    └─ images/                                ← Logo / other images
│
├─ WEB-INF/
│   ├─ classes/                               ← Compiled classes from src/java
│   └─ lib/                                  ← JAR dependencies
│       ├─ gson-2.10.1.jar
│       ├─ jstl-1.2.jar
│       └─ mysql-connector-j-8.0.31.jar
│
├─ AeroVista_Schema.sql                       ← DB schema / tables
├─ aerovista-logo.png                          ← Project logo
└─ README.md                                  ← Project description

```

## 📝 Pages

- **Home Page** (`index.jsp`): Main landing page
- **Business Intelligence** (`bi.jsp`): Analytics and reports
- **Chatbot** (`chatbot.jsp`): AI-powered customer support
- **Feedback** (`feedback.jsp`): Customer feedback management
- **Investment** (`investment.jsp`): Financial tracking
- **Sales** (`sales.jsp`): Sales and booking management
- **Product Form** (`productForm.jsp`): Product management

 
 

 

          

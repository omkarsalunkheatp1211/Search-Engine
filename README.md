# Advanced Search Engine

A powerful search engine implementation with advanced algorithms and modern UI.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Setup](#setup)
- [Recommended Commands](#recommended-commands)
- [API Endpoints](#api-endpoints)

## Features
- **Advanced Search Algorithms**
  - TF-IDF (Term Frequency-Inverse Document Frequency) for content relevance
  - PageRank algorithm for page importance scoring
  - Trie data structure for efficient keyword searching
  - Optimized search results using combined scoring

- **Efficient Data Structures**
  - Trie implementation for fast prefix matching
  - Hash-based document frequency tracking
  - Optimized document indexing

- **Modern User Interface**
  - Clean and responsive design
  - Real-time search suggestions
  - Mobile-friendly layout
  - Loading animations
  - Error handling with user-friendly messages

- **Performance Optimizations**
  - In-memory search index
  - Caching of PageRank scores
  - Efficient tokenization
  - Limited result set size for faster response

## Technologies Used
- **Backend**
  - Java 11
  - Spring Boot 2.x
  - Spring Data JPA
  - Lombok
  - JSoup for web crawling
  - H2/MySQL Database

- **Frontend**
  - HTML5
  - CSS3
  - JavaScript
  - Font Awesome icons

- **Development Tools**
  - Maven
  - Git
  - Spring Tool Suite/IntelliJ IDEA

## Architecture
The search engine implements several key components:

1. **Search Index**
   - Trie-based structure for efficient keyword lookup
   - Document frequency tracking
   - Real-time index updates

2. **Ranking System**
   - TF-IDF calculation for content relevance
   - PageRank implementation for page importance
   - Combined scoring system (70% TF-IDF, 30% PageRank)

3. **Web Crawler**
   - Depth-first crawling strategy
   - Automatic content extraction
   - Link graph building

## Setup

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- MySQL (optional, H2 by default)

### Installation Steps

1. Clone the repository:
```bash
git clone https://github.com/omkarsalunkheatp1211/Search-Engine.git
cd search-engine
```

2. Configure the database:
   - Edit `src/main/resources/application.properties` for your database settings
   - Default uses H2 in-memory database

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

5. Access the application:
   - Open `http://localhost:8080` in your browser

## Recommended Commands

### Development Commands
```bash
# Clean and build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Run tests
mvn test

# Package the application
mvn package
```

### Database Commands
```bash
# Access H2 Console (if using H2)
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:searchdb
# Username: sa
# Password: password
```
